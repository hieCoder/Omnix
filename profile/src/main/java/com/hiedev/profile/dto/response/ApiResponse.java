package com.hiedev.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    int status;
    String message;
    T data;
    String error;
    LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status.value(), message, data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message, String error) {
        return new ApiResponse<>(status.value(), message, null, error, LocalDateTime.now());
    }
}
