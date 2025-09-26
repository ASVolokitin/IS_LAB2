package com.ticket_is.app.exception.notFoundException;

public class CoordinatesNotFoundException extends ResourceNotFoundException {

    public CoordinatesNotFoundException(String message) {
        super(message);
    }

    public CoordinatesNotFoundException(Long id) {
        super(String.format("Coordinates were not found with id %d", id));
    }
    
}
