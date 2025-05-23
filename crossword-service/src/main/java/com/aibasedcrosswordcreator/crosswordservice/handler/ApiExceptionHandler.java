package com.aibasedcrosswordcreator.crosswordservice.handler;

import com.aibasedcrosswordcreator.crosswordservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {
            CrosswordNotFoundException.class,
            ThemeNotFoundException.class,
            LanguageNotFoundException.class,
            WordNotFoundException.class
    })
    public ResponseEntity<Object> notFoundException(Exception e) {
        var status = HttpStatus.NOT_FOUND;
        var response = new ApiExceptionResponse(
                e.getMessage(),
                ZonedDateTime.now(),
                status
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = AiException.class)
    public ResponseEntity<Object> aiException(AiException e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var response = new ApiExceptionResponse(
                e.getMessage(),
                ZonedDateTime.now(),
                status
        );
        return new ResponseEntity<>(response, status);
    }
}
