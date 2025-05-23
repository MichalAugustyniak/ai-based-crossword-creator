package com.aibasedcrosswordcreator.authservice.exception;

public class ApiServerException extends RuntimeException {
    public ApiServerException(String message) {
        super(message);
    }
}
