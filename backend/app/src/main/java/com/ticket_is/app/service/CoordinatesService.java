package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.dto.request.CoordinatesRequest;
import com.ticket_is.app.exception.notFoundException.CoordinatesNotFoundException;
import com.ticket_is.app.model.Coordinates;
import com.ticket_is.app.repository.CoordinatesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;
    
    public List<Coordinates> getALlCoordinates() {
        return coordinatesRepository.findAll();
    }

    public Coordinates getCoordinatesById(Long id) {
        return coordinatesRepository.findById(id)
        .orElseThrow(() -> new CoordinatesNotFoundException(id));
    }

    public void deleteCoordinatesById(Long id) {
        Coordinates coordinates = getCoordinatesById(id);
        coordinatesRepository.delete(coordinates);  
    }

    public void createCoordinates(CoordinatesRequest request) {
        Coordinates coordinates = new Coordinates(request.x(), request.y());
        coordinatesRepository.save(coordinates);
    }

    public void updateCoordinates(Long id, CoordinatesRequest request) {
        Coordinates coordinates = getCoordinatesById(id);
        coordinates.setX(request.x());
        coordinates.setY(request.y());
        coordinatesRepository.save(coordinates);
    }
}
