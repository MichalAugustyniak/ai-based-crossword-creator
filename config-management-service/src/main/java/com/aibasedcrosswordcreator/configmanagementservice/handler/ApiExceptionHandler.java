package com.aibasedcrosswordcreator.configmanagementservice.handler;

import com.aibasedcrosswordcreator.configmanagementservice.exception.ConfigPropertyParseException;
import com.aibasedcrosswordcreator.configmanagementservice.exception.JsonSerializationException;
import com.aibasedcrosswordcreator.configmanagementservice.exception.PropertyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {PropertyNotFoundException.class})
    public ResponseEntity<Object> notFoundException(PropertyNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiExceptionResponse exception = new ApiExceptionResponse(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {ConfigPropertyParseException.class})
    public ResponseEntity<Object> configPropertyParseException(ConfigPropertyParseException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiExceptionResponse exception = new ApiExceptionResponse(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {JsonSerializationException.class})
    public ResponseEntity<Object> jsonSerializationException(JsonSerializationException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiExceptionResponse exception = new ApiExceptionResponse(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }
}
