package com.ticket_is.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticket_is.app.model.Venue;
import com.ticket_is.app.service.VenueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenueController {
    
    private final VenueService venueService;

    @GetMapping
    public List<Venue> getAllCoordinates() {
        return venueService.getAllVenues();
    }

    @GetMapping("/{id}")
    public Venue getCoordinatesById(@PathVariable int id) {
        return venueService.getVenueById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinatesById(@PathVariable int id) {
        venueService.deleteVenueById(id);
        return ResponseEntity.noContent().build();
    }
}
