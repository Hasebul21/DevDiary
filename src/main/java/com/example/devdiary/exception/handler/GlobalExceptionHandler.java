package com.example.devdiary.exception.handler;

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
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements RestExceptionHandler<Exception> {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex) {

        List<String> Error=new ArrayList<>();
        Error.add(ex.getMessage());
        ErrorBody errorBody = new ErrorBody();
        errorBody.setMessage(Error);
        errorBody.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorBody,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
