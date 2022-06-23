package com.example.demo.configuration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class GenericException extends RuntimeException {
    public GenericException(String message) {
        super(message);
    }
}