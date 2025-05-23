package com.aibasedcrosswordcreator.aiservice.communication.exception;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException() {
    }

    public ModelNotFoundException(String message) {
        super(message);
    }
}
