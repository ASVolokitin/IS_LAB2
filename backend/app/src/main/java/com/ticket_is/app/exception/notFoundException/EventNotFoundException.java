package com.ticket_is.app.exception.notFoundException;

public class EventNotFoundException extends ResourceNotFoundException {

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(Integer id) {
        super (String.format("Event was not found with id %d", id));
    }
    
}
