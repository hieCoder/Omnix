package com.hiedev.identity.application.dto.request;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VerifyEmailRequest {
    String totpCode;
    String email;
}
