package com.example.galaxy.security.filter;

import org.springframework.security.core.AuthenticationException;

public class SecurAuthenticationException extends AuthenticationException {
    public SecurAuthenticationException(String msg) {
        super(msg);
    }
    public SecurAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}