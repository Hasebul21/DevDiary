package com.example.devdiary.exception.handler;

import com.example.devdiary.exception.DuplicateEmailException;
import com.example.devdiary.exception.ErrorBody;
import com.example.devdiary.exception.RestExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class DuplicateEmailHandler extends ResponseEntityExceptionHandler
        implements RestExceptionHandler<DuplicateEmailException> {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handle(DuplicateEmailException ex) {

        List<String> Error=new ArrayList<>();
        Error.add(ex.getMessage());
        ErrorBody errorBody = new ErrorBody();
        errorBody.setMessage(Error);
        errorBody.setStatus(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(errorBody,HttpStatus.NOT_ACCEPTABLE);
    }

}
