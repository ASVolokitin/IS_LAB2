package com.ticket_is.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticket_is.app.dto.request.CoordinatesRequest;
import com.ticket_is.app.model.Coordinates;
import com.ticket_is.app.service.CoordinatesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coordinates")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    @GetMapping
    public List<Coordinates> getAllCoordinates() {
        return coordinatesService.getALlCoordinates();
    }

    @GetMapping("/{id}")
    public Coordinates getCoordinatesById(@PathVariable Long id) {
        return coordinatesService.getCoordinatesById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinatesById(@PathVariable Long id) {
        coordinatesService.deleteCoordinatesById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> createCoordinates(@Valid @RequestBody CoordinatesRequest request) {
        coordinatesService.createCoordinates(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoordinates(@PathVariable Long id, @Valid @RequestBody CoordinatesRequest request) {
        coordinatesService.updateCoordinates(id, request);
        return ResponseEntity.noContent().build();
    }
    
}
