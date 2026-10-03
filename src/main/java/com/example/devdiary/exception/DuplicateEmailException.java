package com.example.devdiary.exception;

public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException(String message){

        super(message);
    }
}
