package com.ticket_is.app.exception.notFoundException;

public class VenueNotFoundException extends ResourceNotFoundException {

    public VenueNotFoundException(String message) {
        super(message);
    }

    public VenueNotFoundException(int id) {
        super (String.format("Venue was not found with id %d", id));
    }
    
}