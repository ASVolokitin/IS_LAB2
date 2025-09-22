package com.ticket_is.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticket_is.app.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
    
}
