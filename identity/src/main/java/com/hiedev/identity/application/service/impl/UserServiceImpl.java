package com.hiedev.identity.application.service.impl;

import com.hiedev.identity.application.dto.response.PaginatedResponse;
import com.hiedev.identity.application.dto.response.UserResponse;
import com.hiedev.identity.application.mapper.UserMapper;
import com.hiedev.identity.application.service.UserService;
import com.hiedev.identity.domain.entity.User;
import com.hiedev.identity.domain.repository.UserRepository;
import com.hiedev.identity.presentation.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;

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
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User with id {} not found", id);
            return new NotFoundException("User not found");
        });

        return userMapper.toUserResponseDTO(user);
    }
}
