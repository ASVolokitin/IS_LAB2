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
import com.ticket_is.app.model.Venue;
import com.ticket_is.app.service.VenueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenueController {
    
    private final VenueService venueService;

    @GetMapping("/")
    public List<Venue> getAllVenues() {
        return venueService.getAllVenues();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicketById(@PathVariable Integer id) {
        try{ 
            venueService.deleteVenueById(id);
            return ResponseEntity.ok(String.format("Deleted venue with id %d", id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Venur with id %d was not found", id));
        }
    }
}
