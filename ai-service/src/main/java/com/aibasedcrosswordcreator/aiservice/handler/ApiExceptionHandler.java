package com.aibasedcrosswordcreator.aiservice.handler;

import com.aibasedcrosswordcreator.aiservice.exception.RateLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = RateLimitException.class)
    public ResponseEntity<Object> aiException(Exception e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var response = new ApiExceptionResponse(
                e.getMessage(),
                ZonedDateTime.now(),
                status
        );
        return new ResponseEntity<>(response, status);
    }
}
