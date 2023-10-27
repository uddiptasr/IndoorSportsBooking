package com.booking.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.OK)
public class ResourceNotFoundException extends Exception{
    private static final long serialVersionUID=1L;//not needed
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
