package com.ticket_is.app.dto.request;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventRequest (

    @NotNull
    @NotBlank
    String name,
    
    Date date,

    Integer minAge,

    @NotNull
    String description
    
) {}