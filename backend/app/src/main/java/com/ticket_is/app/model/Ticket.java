package com.ticket_is.app.model;

import java.io.Serializable;

import com.ticket_is.app.model.enums.TicketType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name="tickets")
public class Ticket implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Positive
    @Column(unique=true)
    private long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    
    @NotBlank
    @NotNull
    private String name; //Поле не может быть null, Строка не может быть пустой
    
    @NotNull
    @Valid
    @ManyToOne
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates; //Поле не может быть null
    
    @NotNull
    @Column(name="creation_date")
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    
    @ManyToOne
    @Valid
    @JoinColumn(name = "person_id")
    private Person person; //Поле может быть null
    
    @ManyToOne
    @Valid
    @JoinColumn(name = "event_id")
    private Event event; //Поле может быть null
    
    @NotNull
    @Positive
    private Long price; //Поле не может быть null, Значение поля должно быть больше 0
    
    @Enumerated(EnumType.STRING)
    private TicketType type; //Поле может быть null
    
    @Positive
    @Max(value=100)
    private Float discount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 100
    
    @Positive
    private double number; //Значение поля должно быть больше 0
    
    @NotNull
    private Boolean refundable; //Поле не может быть null
    
    @ManyToOne
    @Valid
    @JoinColumn(name = "venue_id")
    @NotNull
    private Venue venue; //Поле не может быть null
}