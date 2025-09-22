package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.exception.ResourceNotFoundException;
import com.ticket_is.app.model.Venue;
import com.ticket_is.app.repository.VenueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService {
    
    private final VenueRepository venueRepository;

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public void deleteVenueById(Integer id) throws ResourceNotFoundException {
        venueRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Venue was not found with id %d", id)));
        venueRepository.deleteById(id);
    }
}
