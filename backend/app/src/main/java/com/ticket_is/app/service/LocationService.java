package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.dto.request.LocationRequest;
import com.ticket_is.app.exception.notFoundException.LocationNotFoundException;
import com.ticket_is.app.model.Location;
import com.ticket_is.app.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {
    
    private final LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
        .orElseThrow(() -> new LocationNotFoundException(id));
    }

    public void deleteLocationById(Long id) {
        Location location = getLocationById(id);
        locationRepository.delete(location);
    }

    public void createLocation(LocationRequest request) {
        Location location = new Location(request.x(), request.y(), request.z(), request.name());
        locationRepository.save(location);
    }

    public void updateLocation(Long id, LocationRequest request) {
        Location location = getLocationById(id);
        location.setX(request.x());
        location.setY(request.y());
        location.setZ(request.z());
        location.setName(request.name());
        locationRepository.save(location);
    }
}
