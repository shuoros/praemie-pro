package com.scopevisio.praemiepro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class WrongZipcodeException extends RuntimeException {
}
