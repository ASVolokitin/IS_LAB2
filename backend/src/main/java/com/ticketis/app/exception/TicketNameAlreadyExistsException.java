package com.ticketis.app.exception;

public class TicketNameAlreadyExistsException extends RuntimeException {
    public TicketNameAlreadyExistsException(String message) {
        super(message);
    }
}

