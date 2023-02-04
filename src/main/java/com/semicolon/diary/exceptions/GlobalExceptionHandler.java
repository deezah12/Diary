package com.semicolon.diary.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiResponse> GenericHandler(GenericException exception, HttpServletRequest httpServletRequest){
        ApiResponse apiResponse = ApiResponse.builder()
                .timeStamp(ZonedDateTime.now())
                .data(exception.getMessage())
                .path(httpServletRequest.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .isSuccessful(false)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
