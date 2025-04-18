package com.hiedev.identity.presentation.controller;

import com.hiedev.identity.application.dto.request.*;
import com.hiedev.identity.application.dto.response.ApiResponse;
import com.hiedev.identity.application.dto.response.TokenResponse;
import com.hiedev.identity.application.dto.response.UserResponse;
import com.hiedev.identity.application.dto.response.ValidTokenResponse;
import com.hiedev.identity.application.service.AuthService;
import com.hiedev.identity.application.service.impl.TokenBlackListServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.CREATED,
                "Register user success",
                authService.createUser(createUserRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK,
                "Login success",
                authService.login(loginRequest)));
    }

    @PostMapping("/google/callback")
    public ResponseEntity<ApiResponse<TokenResponse>> loginOauth2(@RequestParam String code) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,
                "Login success",
                authService.loginOauth2(code)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Boolean>> logout(Authentication authentication, @RequestBody RefreshTokenRequest refreshToken) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK,
                "Logout success",
                authService.logout(authentication, refreshToken)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK,
                "Refresh token success",
                authService.refreshToken(refreshToken)));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<TokenResponse>> verifyEmail(@RequestBody VerifyEmailRequest verifyEmailRequest) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,
                "Verify email success",
                authService.verifyEmail(verifyEmailRequest)));
    }

    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<ValidTokenResponse>> introspect(@RequestBody ValidTokenRequest validTokenRequest) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,
                "Introspect token success",
                authService.introspect(validTokenRequest)));
    }

}
