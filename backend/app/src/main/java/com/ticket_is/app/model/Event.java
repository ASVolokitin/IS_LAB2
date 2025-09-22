package com.ticket_is.app.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name="events")
public class Event implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @NotNull
    @Positive
    @Column(unique=true)
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    
    @NotNull
    @NotBlank
    private String name; //Поле не может быть null, Строка не может быть пустой

    private java.util.Date date; //Поле может быть null

    @Column(name="min_age")
    private Integer minAge; //Поле может быть null

    @NotNull
    private String description; //Поле не может быть null
}