package com.aibasedcrosswordcreator.crosswordservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CrosswordNotFoundException extends RuntimeException {

    public CrosswordNotFoundException(String message) {
        super(message);
    }

    public CrosswordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
