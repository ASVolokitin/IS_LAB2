package com.ticket_is.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticket_is.app.exception.ResourceNotFoundException;
import com.ticket_is.app.model.Ticket;
import com.ticket_is.app.service.TicketService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/")
    public List<Ticket> getALlTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public ResponseEntity getTicketById(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.getTicketByIdOrThrow(id);
            return ResponseEntity.ok(ticket);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicketById(@PathVariable Long id) {
        try{ 
            ticketService.deleteTicketById(id);
            return ResponseEntity.ok(String.format("Deleted ticket with id %d", id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Ticket with id %d was not found", id));
        }
    }
    
    
}
