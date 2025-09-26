package com.ticket_is.app.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {
    
    private final LocalDateTime timestamp;
    private final String message;
    private final String details;
}
