package com.ticket_is.app.model;

import java.io.Serializable;

import com.ticket_is.app.model.enums.VenueType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name="venues")
public class Venue implements Serializable{
    
    @Id
    @Positive
    @Column(unique=true)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    
    private String name; //Поле не может быть null, Строка не может быть пустой
    
    private Integer capacity; //Поле не может быть null, Значение поля должно быть больше 0
    
    @Enumerated(EnumType.STRING)
    private VenueType type; //Поле может быть null
}