package com.ticket_is.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ticket_is.app.dto.request.PersonRequest;
import com.ticket_is.app.exception.notFoundException.PersonNotFoundException;
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

    public Person getPersonById(Long id) {
        return personRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException(id));
    }

    public void deletePersonById(Long id) {
        Person person = getPersonById(id);
        personRepository.delete(person);
    }

    public void createPerson(PersonRequest request) {
        Person person = new Person(request.getEyeColor(), request.getHairColor(), request.location(), request.passportID(), request.nationality());
        personRepository.save(person);
    }
    
}
