package com.example.devdiary.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public interface RestExceptionHandler<T> {
    ResponseEntity<Object> handle(T ex);
}
