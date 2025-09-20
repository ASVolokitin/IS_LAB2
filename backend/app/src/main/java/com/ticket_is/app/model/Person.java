package com.ticket_is.app.model;

import java.io.Serializable;

import com.ticket_is.app.model.enums.Color;
import com.ticket_is.app.model.enums.Country;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="persons")
public class Person implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name="eye_color")
    private Color eyeColor; //Поле не может быть null
    @Enumerated(EnumType.STRING)
    @Column(name="hair_color")
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле может быть null
    @Column(name="passport_id")
    private String passportID; //Строка не может быть пустой, Длина строки не должна быть больше 29, Значение этого поля должно быть уникальным, Поле может быть null
    @Enumerated(EnumType.STRING)
    private Country nationality; //Поле может быть null
}