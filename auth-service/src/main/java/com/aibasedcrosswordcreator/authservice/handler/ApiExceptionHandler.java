package com.aibasedcrosswordcreator.authservice.handler;

import com.aibasedcrosswordcreator.authservice.exception.ApiClientException;
import com.aibasedcrosswordcreator.authservice.exception.ApiServerException;
import com.aibasedcrosswordcreator.authservice.exception.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {NotImplementedException.class})
    public ResponseEntity<Object> notImplementedException(NotImplementedException e) {
        HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
        ApiException exception = new ApiException(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {ApiServerException.class})
    public ResponseEntity<Object> serverException(ApiServerException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException exception = new ApiException(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {ApiClientException.class})
    public ResponseEntity<Object> clientError(ApiClientException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException exception = new ApiException(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exception, status);
    }
}
