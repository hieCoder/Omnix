package com.hiedev.identity.application.service;

import com.hiedev.identity.application.dto.request.*;
import com.hiedev.identity.application.dto.response.TokenResponse;
import com.hiedev.identity.application.dto.response.UserResponse;
import com.hiedev.identity.application.dto.response.ValidTokenResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    UserResponse createUser(CreateUserRequest createUserRequest);
    TokenResponse login(LoginRequest loginRequest);
    TokenResponse loginOauth2(String code);
    boolean logout(Authentication authentication, RefreshTokenRequest refreshToken);
    TokenResponse refreshToken(RefreshTokenRequest refreshToken);
    TokenResponse verifyEmail(VerifyEmailRequest verifyEmailRequest);
    ValidTokenResponse introspect(ValidTokenRequest validTokenRequest);
}
