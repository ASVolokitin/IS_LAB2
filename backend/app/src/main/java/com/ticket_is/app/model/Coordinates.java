package com.ticket_is.app.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Coordinates implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value=-200)
    private Integer x; //Значение поля должно быть больше -201, Поле не может быть null

    @NotNull
    @Min(value=-4)
    private Double y; //Значение поля должно быть больше -5, Поле не может быть null
}