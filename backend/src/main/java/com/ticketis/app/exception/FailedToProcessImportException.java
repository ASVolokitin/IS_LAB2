package com.ticketis.app.exception;

public class FailedToProcessImportException extends RuntimeException {

    public FailedToProcessImportException(String message) {
        super("Failed to process import " + message);
    }
    
}
