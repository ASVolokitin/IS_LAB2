package com.ticket_is.app.exception.notFoundException;

public class PersonNotFoundException extends ResourceNotFoundException {

    public PersonNotFoundException(String message) {
        super(message);
    }

    public PersonNotFoundException(Long id) {
        super (String.format("Person was not found with id %d", id));
    }
}
