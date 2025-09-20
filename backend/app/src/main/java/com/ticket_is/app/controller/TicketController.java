package com.ticket_is.app.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticket_is.app.exception.ResourceNotFoundException;
import com.ticket_is.app.model.Ticket;
import com.ticket_is.app.service.TicketService;


@RestController
@RequestMapping("/main")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService collectionService) {
        this.ticketService = collectionService;
    }

    @GetMapping("/list")
    public List<Ticket> getALlTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public ResponseEntity getTicketById(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.getTicketByIdOrThrow(id);
            return ResponseEntity.ok(ticket);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            error.put("timestamp", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
    }
    
    
}
