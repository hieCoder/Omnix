package com.hiedev.profile.exception;

public class TOTPException extends RuntimeException {
    public TOTPException(String message) {
        super(message);
    }
    public TOTPException(String message, Throwable cause) {
        super(message, cause);
    }
}
