package com.cloud.work.exception;

import lombok.Getter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

@Getter
public class UserNotFoundException extends InternalAuthenticationServiceException {
    private final String code;

    public UserNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
}