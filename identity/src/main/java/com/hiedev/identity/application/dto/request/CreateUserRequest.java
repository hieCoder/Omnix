package com.hiedev.identity.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    String firstName;
    String lastName;

    LocalDate dob;

    @NotBlank(message = "Email must be not null")
    @Email(message = "Email invalid format")
    String email;

    @NotBlank(message = "Password must be not null")
    String password;
}
