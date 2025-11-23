package com.ticketis.app.exception;

public class FailedToUploadFileException extends RuntimeException {

    public FailedToUploadFileException(String message) {
        super("Failed to upload file: " + message);
    }
    
}
