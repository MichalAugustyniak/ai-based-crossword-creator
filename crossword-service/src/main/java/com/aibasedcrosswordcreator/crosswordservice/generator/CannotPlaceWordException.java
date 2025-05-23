package com.aibasedcrosswordcreator.crosswordservice.generator;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotPlaceWordException extends RuntimeException {
    public CannotPlaceWordException(String message) {
        super(message);
    }
}
