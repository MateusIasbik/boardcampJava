package com.boardcamp.api.exceptions;

public class CustomerIdNotfoundException extends RuntimeException {
    
    public CustomerIdNotfoundException(String message) {
        super(message);
    }
}