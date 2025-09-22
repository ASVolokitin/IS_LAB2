package com.ticket_is.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticket_is.app.exception.ResourceNotFoundException;
import com.ticket_is.app.model.Person;
import com.ticket_is.app.service.PersonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicketById(@PathVariable Long id) {
        try{ 
            personService.deletePersonById(id);
            return ResponseEntity.ok(String.format("Deleted person with id %d", id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Person with id %d was not found", id));
        }
    }

}
