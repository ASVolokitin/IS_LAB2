package com.ticket_is.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticket_is.app.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    public Ticket findByName(String name);
}