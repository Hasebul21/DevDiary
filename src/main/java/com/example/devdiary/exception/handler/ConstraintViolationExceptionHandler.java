package com.example.devdiary.exception.handler;

import com.example.devdiary.exception.ErrorBody;
import com.example.devdiary.exception.RestExceptionHandler;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Order(1)
@Component
public class ConstraintViolationExceptionHandler extends ResponseEntityExceptionHandler
        implements RestExceptionHandler<ConstraintViolationException> {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handle(ConstraintViolationException ex) {
        List<String> Error = new ArrayList<>();
        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            Error.add(violation.getPropertyPath() + " : " + violation.getMessage());
        }
        ErrorBody errorBody = new ErrorBody();
        errorBody.setMessage(Error);
        errorBody.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }
}
