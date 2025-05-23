package com.aibasedcrosswordcreator.crosswordservice.exception;

public class AiException extends RuntimeException {
    public AiException() {
    }

    public AiException(String message) {
        super(message);
    }
}
