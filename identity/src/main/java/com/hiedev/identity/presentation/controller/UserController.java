package com.hiedev.identity.presentation.controller;

import com.hiedev.identity.application.dto.response.ApiResponse;
import com.hiedev.identity.application.dto.response.PaginatedResponse;
import com.hiedev.identity.application.dto.response.UserResponse;
import com.hiedev.identity.application.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<UserResponse>>> findAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK,
                "Get list user success",
                userService.findAllUsers(page, size, sortBy, sortDir)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> findUserById() {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK,
                "Get user success",
                userService.getUserDetail()));
    }

}
