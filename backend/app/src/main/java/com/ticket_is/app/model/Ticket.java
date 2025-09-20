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
import lombok.Data;

@Data
@Entity
@Table(name="tickets")
public class Ticket implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    @ManyToOne
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates; //Поле не может быть null
    @Column(name="creation_date")
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person; //Поле может быть null
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; //Поле может быть null
    private Long price; //Поле не может быть null, Значение поля должно быть больше 0
    @Enumerated(EnumType.STRING)
    private TicketType type; //Поле может быть null
    private Float discount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 100
    private double number; //Значение поля должно быть больше 0
    private Boolean refundable; //Поле не может быть null
    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue; //Поле не может быть null
}