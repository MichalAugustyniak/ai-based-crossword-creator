package com.aibasedcrosswordcreator.userservice.handler;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiException(String message, ZonedDateTime timestamp, HttpStatus status) {
}
