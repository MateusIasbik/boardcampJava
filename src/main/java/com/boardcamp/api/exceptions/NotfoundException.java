package com.boardcamp.api.exceptions;

public class NotfoundException extends RuntimeException {
    
    public NotfoundException(String message) {
        super(message);
    }
}