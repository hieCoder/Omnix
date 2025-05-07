package com.hiedev.identity.application.service.impl;

import com.hiedev.identity.application.dto.response.PaginatedResponse;
import com.hiedev.identity.application.dto.response.ProfileResponse;
import com.hiedev.identity.application.dto.response.UserResponse;
import com.hiedev.identity.application.mapper.UserMapper;
import com.hiedev.identity.application.service.UserService;
import com.hiedev.identity.domain.entity.User;
import com.hiedev.identity.domain.repository.UserRepository;
import com.hiedev.identity.infrastructure.client.openfeign.ProfileClient;
import com.hiedev.identity.presentation.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    ProfileClient profileClient;

    @PreAuthorize("hasRole('USER')")
    @Override
    public PaginatedResponse<UserResponse> findAllUsers(int page, int site, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, site, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponse> listUser = userPage.getContent().stream()
                .map(userMapper::toUserResponseDTO).toList();

        return new PaginatedResponse<>(
                listUser,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                !userPage.isLast());
    }

    @Override
    public UserResponse getUserDetail() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User with email {} not found", email);
            return new NotFoundException("User not found");
        });

        ResponseEntity<ProfileResponse> profile = profileClient.getProfile(user.getId());

        if (!profile.getStatusCode().is2xxSuccessful()) {
            log.error("Profile not found for userId: {}", user.getId());
            throw new NotFoundException("Profile not found");
        }

        UserResponse userResponse = userMapper.toUserResponseDTO(user);
        userResponse.setFirstName(profile.getBody().getFirstName());
        userResponse.setLastName(profile.getBody().getLastName());

        return userResponse;
    }
}
