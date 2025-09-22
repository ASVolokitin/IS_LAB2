package com.ticket_is.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticket_is.app.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>{
    
}
