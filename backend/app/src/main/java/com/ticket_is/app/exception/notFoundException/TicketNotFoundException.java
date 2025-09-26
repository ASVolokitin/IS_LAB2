package com.ticket_is.app.exception.notFoundException;

public class TicketNotFoundException extends ResourceNotFoundException {

    public TicketNotFoundException(String message) {
        super(message);
    }

    public TicketNotFoundException(Long id) {
        super (String.format("Ticket was not found with id %d", id));
    }
    
}