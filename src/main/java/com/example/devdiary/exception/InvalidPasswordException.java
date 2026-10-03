package com.example.devdiary.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super(
                "Invalid Password: must be at least 8 characters and contain an uppercase letter, a"
                        + " lowercase letter and a digit");
    }
}
