package com.aibasedcrosswordcreator.crosswordservice.exception;

public class InvalidRequestParamException extends RuntimeException {
    public InvalidRequestParamException() {
    }

    public InvalidRequestParamException(String message) {
        super(message);
    }
}
