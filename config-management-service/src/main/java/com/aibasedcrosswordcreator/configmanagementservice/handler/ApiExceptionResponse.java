package com.aibasedcrosswordcreator.configmanagementservice.handler;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiExceptionResponse(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
}
