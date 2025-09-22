package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.exception.ResourceNotFoundException;
import com.ticket_is.app.model.Ticket;
import com.ticket_is.app.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketByIdOrThrow(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
    }

    public void deleteTicketById(Long id) throws ResourceNotFoundException {
        ticketRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Ticket was not found with id %d", id)));
        ticketRepository.deleteById(id);
    }

    public void saveOrUpdate(Ticket ticket) {
        ticketRepository.save(ticket);
    }
}
