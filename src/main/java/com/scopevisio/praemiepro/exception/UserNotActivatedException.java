package com.scopevisio.praemiepro.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {

    public UserNotActivatedException(final String email) {
        super("User with email: " + email + " is not activated");
    }
}
