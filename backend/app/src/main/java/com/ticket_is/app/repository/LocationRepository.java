package com.ticket_is.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket_is.app.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
    
}
