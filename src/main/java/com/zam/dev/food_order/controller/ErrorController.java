package com.zam.dev.food_order.controller;

import com.zam.dev.food_order.model.WebResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class ErrorController {


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationExceptions(ConstraintViolationException constraintViolationException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder().
                        message(constraintViolationException.getMessage()).
                        status(HttpStatus.BAD_REQUEST.value()).
                        build());
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> responseStatusException (ResponseStatusException responseStatusException , HttpServletResponse response){
        String message = responseStatusException.getMessage();
        int responseStatus = responseStatusException.getStatusCode().value();
        WebResponse<Object> webResponse = WebResponse.builder()
                .data(null)
                .message(message)
                .status(responseStatus)
                .build();
        return ResponseEntity.status(responseStatusException.getStatusCode()).body(webResponse);
    }
}
