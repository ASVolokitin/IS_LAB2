package com.ticket_is.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticket_is.app.model.Event;
import com.ticket_is.app.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<Event> getAllCoordinates() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getCoordinatesById(@PathVariable Integer id) {
        return eventService.getEventById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinatesById(@PathVariable Integer id) {
        eventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }
        
}
