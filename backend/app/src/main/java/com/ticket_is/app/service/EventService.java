package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.exception.ResourceNotFoundException;
import com.ticket_is.app.model.Event;
import com.ticket_is.app.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void deleteEventById(Integer id) throws ResourceNotFoundException {
        eventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Event was not found with id %d", id)));
        eventRepository.deleteById(id);
    }
    
}
