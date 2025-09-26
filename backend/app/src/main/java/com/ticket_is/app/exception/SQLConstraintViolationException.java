package com.ticket_is.app.exception;

public class SQLConstraintViolationException extends RuntimeException {

    public SQLConstraintViolationException(String message){
        super(message);
    }
    
}
