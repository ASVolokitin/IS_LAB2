package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.exception.ResourceNotFoundException;
import com.ticket_is.app.model.Person;
import com.ticket_is.app.repository.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {
    
    private final PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public void deletePersonById(Long id) throws ResourceNotFoundException {
        personRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Person was not found with id %d", id)));
        personRepository.deleteById(id);
    }
}
