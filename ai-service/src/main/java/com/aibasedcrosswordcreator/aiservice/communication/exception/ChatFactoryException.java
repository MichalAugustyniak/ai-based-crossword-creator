package com.aibasedcrosswordcreator.aiservice.communication.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatFactoryException extends RuntimeException {
    public ChatFactoryException(String message) {
        super(message);
    }
}
