package com.example.devdiary.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz, String key, String value) {
        super(clazz.getSimpleName() + " not found with " + key + " " + value);
    }
}
