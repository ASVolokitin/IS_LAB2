package com.ticketis.app.exception.importBusinessException;

import java.util.List;

public class FileImportValidationException extends ImportBusinessException {
    private final List<String> errors;
    
    public FileImportValidationException(List<String> errors) {
        super("Import validation failed with " + errors.size() + " errors");
        this.errors = errors;
    }
    
    public List<String> getErrors() {
        return errors;
    }
}