package com.hiedev.profile.exception;

public class AuthenticationServiceException extends RuntimeException {
    public AuthenticationServiceException(String message) {
        super(message);
    }
}
