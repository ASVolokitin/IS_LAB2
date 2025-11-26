package com.ticketis.app.exception.notfoundexception;

public class FileImportRecordNotFoundException extends ResourceNotFoundException {

    public FileImportRecordNotFoundException(String message) {
        super(message);
    }

    public FileImportRecordNotFoundException(Long id) {
        super(String.format("Import record was not found with id %d", id));
    }
    
}