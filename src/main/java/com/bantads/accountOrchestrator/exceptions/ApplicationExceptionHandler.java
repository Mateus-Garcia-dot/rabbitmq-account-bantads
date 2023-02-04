package com.bantads.accountOrchestrator.exceptions;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

@Data
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseStatusExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity handleException(HttpClientErrorException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }

}
