package com.aibasedcrosswordcreator.crosswordservice.handler;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiExceptionResponse(String message,
                                   ZonedDateTime timestamp,
                                   HttpStatus status) {
}
