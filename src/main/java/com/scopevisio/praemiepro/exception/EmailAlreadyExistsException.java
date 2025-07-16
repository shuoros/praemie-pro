package com.scopevisio.praemiepro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends AuthenticationException {

    public EmailAlreadyExistsException(final String email) {
        super("User with email: " + email + " is already exists");
    }
}
