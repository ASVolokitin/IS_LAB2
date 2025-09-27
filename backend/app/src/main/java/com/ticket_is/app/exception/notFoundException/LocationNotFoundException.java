package com.ticket_is.app.exception.notFoundException;

public class LocationNotFoundException extends ResourceNotFoundException {

    public LocationNotFoundException(String message) {
        super(message);
    }

    public LocationNotFoundException(Long id) {
        super (String.format("Location was not found with id %d", id));
    }
}
