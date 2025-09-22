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
import com.ticket_is.app.model.Event;
import com.ticket_is.app.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicketById(@PathVariable Integer id) {
        try{ 
            eventService.deleteEventById(id);
            return ResponseEntity.ok(String.format("Deleted event with id %d", id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Event with id %d was not found", id));
        }
    }
    
}
