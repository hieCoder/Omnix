package com.hiedev.identity.application.service.impl;

import com.hiedev.event.EmailRequest;
import com.hiedev.event.RecipientRequest;
import com.hiedev.identity.application.dto.request.*;
import com.hiedev.identity.application.dto.response.GoogleUserInfoResponse;
import com.hiedev.identity.application.dto.response.TokenResponse;
import com.hiedev.identity.application.dto.response.UserResponse;
import com.hiedev.identity.application.dto.response.ValidTokenResponse;
import com.hiedev.identity.application.mapper.UserMapper;
import com.hiedev.identity.application.service.AuthService;
import com.hiedev.identity.application.service.GoogleService;
import com.hiedev.identity.domain.entity.Permission;
import com.hiedev.identity.domain.entity.Provider;
import com.hiedev.identity.domain.entity.Role;
import com.hiedev.identity.domain.entity.User;
import com.hiedev.identity.domain.enums.PermissionEnum;
import com.hiedev.identity.domain.enums.RoleEnum;
import com.hiedev.identity.domain.enums.UserProviderEnum;
import com.hiedev.identity.domain.enums.UserStatusEnum;
import com.hiedev.identity.domain.repository.ProviderRepository;
import com.hiedev.identity.domain.repository.RoleRepository;
import com.hiedev.identity.domain.repository.UserRepository;
import com.hiedev.identity.infrastructure.client.openfeign.ProfileClient;
import com.hiedev.identity.infrastructure.security.CustomUserDetail;
import com.hiedev.identity.infrastructure.security.JwtTokenProvider;
import com.hiedev.identity.presentation.exception.DuplicateException;
import com.hiedev.identity.presentation.exception.NotFoundException;
import com.hiedev.identity.presentation.exception.TOTPException;
import com.hiedev.identity.presentation.exception.TokenGenerationException;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    JwtTokenProvider jwtTokenProvider;
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    ProviderRepository providerRepository;
    GoogleService googleService;
    JwtDecoder jwtDecoder;
    TokenBlackListServiceImpl tokenBlackListServiceImpl;
    TotpServiceImpl totpServiceImpl;
    RedisTemplate<String, String> redisTemplate;
    KafkaTemplate<String, EmailRequest> kafkaTemplate;

    String TOTP_KEY_PREFIX = "totp:";

    @Transactional
    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        // Check if the email already exists
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new DuplicateException("Email already exists");
        }

        // Create a new user
        User user = userMapper.toUser(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRoleName(RoleEnum.USER).orElseThrow(() -> new NotFoundException("Role not found"))));
        user = userRepository.save(user);

        // Send a message to Kafka save profile event
        ProfileRequest profileRequest = userMapper.toUserEventResponseDTO(createUserRequest);
        profileRequest.setUserId(user.getId());
        profileClient.createProfile(profileRequest);

        // Generate TOTP secret key and store it in Redis
        String secretKey = totpServiceImpl.generateSecretKey();
        String toptCode = totpServiceImpl.generateTOTP(secretKey);

        redisTemplate.opsForValue().set(TOTP_KEY_PREFIX + user.getEmail(), secretKey, 300, TimeUnit.SECONDS);

        // Send a welcome email
        EmailRequest emailRequest = EmailRequest.builder()
                .to(RecipientRequest.builder().email(user.getEmail()).name(createUserRequest.getFirstName()).build())
                .subject("Welcome to HieDev")
                .htmlContent("<h1>Chào mừng đến với HieDev</h1>" +
                        "<p>Hi " + createUserRequest.getFirstName() + ",</p>" +
                        "<p>Cảm ơn bạn đã đăng ký! Đây là mã OTP của bạn để xác nhận tài khoản:</p>" +
                        "<h2>" + toptCode + "</h2>" +
                        "<p>Mã này có hiệu lực trong 5 phút. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>")
                .build();
        kafkaTemplate.send("user-registration", emailRequest);

        log.info("Welcome email sent to {}", user.getEmail());
        UserResponse userResponse = userMapper.toUserResponseDTO(user);
        userResponse.setFirstName(createUserRequest.getFirstName());
        userResponse.setLastName(createUserRequest.getLastName());
        return userResponse;
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Generate JWT tokens
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return generateToken(user.getUsername(), user.getRoles(), user.getPermissions());
    }

    @Transactional
    @Override
    public TokenResponse loginOauth2(String code) {
        // Get user info from Google
        GoogleUserInfoResponse googleUserInfoResponse = googleService.getUserInfo(code);

        // Check if the user already exists
        User user = userRepository.findByEmail(googleUserInfoResponse.getEmail()).orElseGet(() -> {
            // Create a new user if not found
            User newUser = User.builder()
                    .email(googleUserInfoResponse.getEmail())
                    .roles(Set.of(roleRepository.findByRoleName(RoleEnum.USER).orElseThrow(() -> new NotFoundException("Role not found"))))
                    .status(UserStatusEnum.ACTIVE)
                    .build();

            // Send a message to Kafka save profile event
            profileClient.createProfile(ProfileRequest.builder()
                    .userId(newUser.getId())
                    .lastName(googleUserInfoResponse.getFamilyName())
                    .firstName(googleUserInfoResponse.getGivenName())
                    .build());

            return userRepository.save(newUser);
        });

        // Check if the provider already exists
        if (!providerRepository.existsByProviderId(googleUserInfoResponse.getId())) {
            Provider provider = Provider.builder()
                    .providerId(googleUserInfoResponse.getId())
                    .user(user)
                    .provider(UserProviderEnum.GOOGLE)
                    .build();
            providerRepository.save(provider);
        }

        return generateToken(user.getEmail(), getRoles(user.getRoles()), getPermissions(user.getRoles()));
    }

    @Override
    public boolean logout(Authentication authentication, RefreshTokenRequest refreshToken) {
        // Decode the JWT tokens
        Jwt accessToken = authentication.getPrincipal() instanceof Jwt ? (Jwt) authentication.getPrincipal() : null;
        Jwt refreshTokenJwt = jwtDecoder.decode(refreshToken.getRefreshToken());

        // Check if the tokens are valid
        if (accessToken != null && refreshTokenJwt != null) {
            // Add to BlackList the tokens
            tokenBlackListServiceImpl.blackListToken(accessToken.getId(), accessToken.getExpiresAt());
            tokenBlackListServiceImpl.blackListToken(refreshTokenJwt.getId(), refreshTokenJwt.getExpiresAt());
            return true;
        }

        return false;
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest refreshToken) {
        // Decode the refresh token
        Jwt jwt = jwtDecoder.decode(refreshToken.getRefreshToken());

        if (jwt == null) {
            throw new NotFoundException("Refresh token not found");
        }

        // Generate a new access token
        User user = userRepository.findByEmail(jwt.getSubject()).orElseThrow(()
                -> new NotFoundException("User not found"));

        tokenBlackListServiceImpl.blackListToken(jwt.getId(), jwt.getExpiresAt());

        return generateToken(user.getEmail(), getRoles(user.getRoles()), getPermissions(user.getRoles()));
    }

    @Override
    public TokenResponse verifyEmail(VerifyEmailRequest verifyEmailRequest) {
        if (verifyEmailRequest.getTotpCode() == null || verifyEmailRequest.getEmail() == null) {
            throw new NotFoundException("TOTP code or email not found");
        }

        String email = verifyEmailRequest.getEmail();
        String totpCode = verifyEmailRequest.getTotpCode();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        String secretKey = redisTemplate.opsForValue().get(TOTP_KEY_PREFIX + email);
        if (!totpServiceImpl.validateTOTP(secretKey, totpCode)) throw new TOTPException("Invalid TOTP code");
        user.setStatus(UserStatusEnum.ACTIVE);
        userRepository.save(user);

        return generateToken(user.getEmail(), getRoles(user.getRoles()), getPermissions(user.getRoles()));
    }

    @Override
    public ValidTokenResponse introspect(ValidTokenRequest validTokenRequest) {
        Jwt jwt = jwtDecoder.decode(validTokenRequest.getToken());
        if (jwt == null) {
            throw new NotFoundException("Token not found");
        }
        return ValidTokenResponse.builder().valid(true).build();
    }

    private Set<RoleEnum> getRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
    }

    private Set<PermissionEnum> getPermissions(Set<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getPermissionName)
                .collect(Collectors.toSet());
    }

    private TokenResponse generateToken(String email, Set<RoleEnum> roleEnums, Set<PermissionEnum> permissionEnums) {
        try {
            String accessToken = jwtTokenProvider.generateAccessToken(email, roleEnums, permissionEnums);
            String refreshToken = jwtTokenProvider.generateRefreshToken(email, roleEnums, permissionEnums);

            return new TokenResponse(accessToken, refreshToken);
        } catch (JOSEException e) {
            log.error("Failed to generate token for user: {}", email, e);
            throw new TokenGenerationException("Token generation failed", e);
        }
    }
}
