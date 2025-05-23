package com.aibasedcrosswordcreator.userservice.handler;

import com.aibasedcrosswordcreator.userservice.exception.NotImplementedException;
import com.aibasedcrosswordcreator.userservice.exception.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(UserNotFoundException e) {
        var status = HttpStatus.NOT_FOUND;
        var exception = new ApiException(
                e.getMessage(),
                ZonedDateTime.now(),
                status
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = NotImplementedException.class)
    public ResponseEntity<Object> notImplementedException(UserNotFoundException e) {
        var status = HttpStatus.NOT_FOUND;
        var exception = new ApiException(
                e.getMessage(),
                ZonedDateTime.now(),
                status
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    public ResponseEntity<Object> jsonProcessingException(JsonProcessingException e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var exception = new ApiException(
                "Server-side JSON processing error",
                ZonedDateTime.now(),
                status
        );
        return new ResponseEntity<>(exception, status);
    }
}
