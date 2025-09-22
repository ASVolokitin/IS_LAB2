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
import com.ticket_is.app.model.Coordinates;
import com.ticket_is.app.service.CoordinatesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coordinates")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    @GetMapping("/")
    public List<Coordinates> getAllCoordinates() {
        return coordinatesService.getALlTickets();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoordinatesById(@PathVariable Long id) {
        try{ 
            coordinatesService.deleteCoordinatesById(id);
            return ResponseEntity.ok(String.format("Deleted coordinates with id %d", id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Coordinates with id %d were not found", id));
        }
    }
    
}
