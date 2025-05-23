package com.aibasedcrosswordcreator.crosswordservice.exception;

public class ClueNotFoundException extends RuntimeException {
    public ClueNotFoundException() {
    }

    public ClueNotFoundException(String message) {
        super(message);
    }
}
