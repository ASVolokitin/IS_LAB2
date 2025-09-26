package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.exception.notFoundException.EventNotFoundException;
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

    public Event getEventById(Integer id) {
        return eventRepository.findById(id)
        .orElseThrow(() -> new EventNotFoundException(id));
    }

    public void deleteEventById(Integer id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }
    
}