package com.raksmey.jtw.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestExceptionHandler extends RuntimeException {

    public BadRequestExceptionHandler(String message) {
        super(message);
    }
}
