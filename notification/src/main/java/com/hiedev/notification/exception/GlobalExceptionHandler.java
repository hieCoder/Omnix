package com.hiedev.notification.exception;

import com.hiedev.notification.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private <T extends Exception> ApiResponse<?> handleException(HttpStatus status, String title, T ex, String logLevel) {
        String message = ex.getMessage() != null ? ex.getMessage() : title;
        switch (logLevel.toLowerCase()) {
            case "error":
                log.error("{}: {}", title, message, ex);
                break;
            case "warn":
                log.warn("{}: {}", title, message);
                break;
            default:
                log.info("{}: {}", title, message);
        }
        return ApiResponse.error(status, title, message);
    }

    // Xử lý tổng quát (500 INTERNAL SERVER ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> exception(Exception e) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e, "error");
    }

    // Xử lý lỗi validation error (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        log.warn("Validation Error: {}", e.getMessage());
        return ApiResponse.error(HttpStatus.BAD_REQUEST, "Validation Error", errorMsg);
    }

    // Xử lý lỗi Resource Not Found (404 NOT FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> notFoundException(NotFoundException e) {
        return handleException(HttpStatus.NOT_FOUND, "Not Found", e, "warn");
    }

    // Xử lý lỗi Unauthorized (401 Unauthorized)
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> authenticationException(AuthenticationException e) {
        return handleException(HttpStatus.UNAUTHORIZED, "Unauthorized", e, "warn");
    }

    // Xử lý lỗi Access Denied (403 FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> accessDeniedException(AccessDeniedException e) {
        return handleException(HttpStatus.FORBIDDEN, "Forbidden", e, "warn");
    }

    // Xử lý tài khoản bị khóa (403 Forbidden)
    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> lockedException(LockedException e) {
        return handleException(HttpStatus.FORBIDDEN, "Account Locked", e, "warn");
    }

    // Xử lý tài khoản bị vô hiệu hóa (403 Forbidden)
    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> disabledException(DisabledException e) {
        return handleException(HttpStatus.FORBIDDEN, "Account Disabled", e, "warn");
    }

    // Xử lý lỗi Conflict Error (409 Conflict)
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> conflictException(ConflictException e) {
        return handleException(HttpStatus.CONFLICT, "Conflict", e, "warn");
    }

    // Xử lý lỗi tham số không hợp lệ
    @ExceptionHandler(java.lang.IllegalAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> illegalAccessException(java.lang.IllegalAccessException e) {
        log.warn("Illegal Access: {}", e.getMessage());
        return ApiResponse.error(HttpStatus.BAD_REQUEST, "Illegal Access", e.getMessage());
    }

    // Xử lý lỗi tham số không hợp lệ
    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> duplicateException(DuplicateException e) {
        return handleException(HttpStatus.CONFLICT, "Duplicate", e, "warn");
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleAuthenticationServiceException(AuthenticationServiceException e) {
        return handleException(HttpStatus.UNAUTHORIZED, "Unauthorized", e, "error");
    }
}

