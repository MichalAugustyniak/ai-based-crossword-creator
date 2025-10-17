package com.aibasedcrosswordcreator.crosswordservice.exception;

public class ServerException extends RuntimeException {
    public ServerException() {
    }

    public ServerException(String message) {
        super(message);
    }
}
