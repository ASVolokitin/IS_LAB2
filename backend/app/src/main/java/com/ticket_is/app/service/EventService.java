package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.dto.request.EventRequest;
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

    public void createEvent(EventRequest request) {
        Event event = new Event(request.name(), request.date(), request.minAge(), request.description());
        eventRepository.save(event);
    }

    public void updateEvent(Integer id, EventRequest request) {
        Event event = getEventById(id);
        event.setName(request.name());
        event.setDate(request.date());
        event.setMinAge(request.minAge());
        event.setDescription(request.description());
        eventRepository.save(event);
    }
    
}