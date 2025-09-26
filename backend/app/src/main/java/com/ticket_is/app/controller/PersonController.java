package com.ticket_is.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticket_is.app.model.Person;
import com.ticket_is.app.service.PersonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<Person> getAllCoordinates() {
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    public Person getCoordinatesById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinatesById(@PathVariable Long id) {
        personService.deletePersonById(id);
        return ResponseEntity.noContent().build();
    }
        
}
