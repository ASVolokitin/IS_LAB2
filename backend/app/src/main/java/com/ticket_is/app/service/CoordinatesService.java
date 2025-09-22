package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.exception.ResourceNotFoundException;
import com.ticket_is.app.model.Coordinates;
import com.ticket_is.app.repository.CoordinatesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;
    
    public List<Coordinates> getALlTickets() {
        return coordinatesRepository.findAll();
    }

    public void deleteCoordinatesById(Long id) throws ResourceNotFoundException {
        coordinatesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Coordinates were not found with id %d", id)));
        coordinatesRepository.deleteById(id);
    }
}
