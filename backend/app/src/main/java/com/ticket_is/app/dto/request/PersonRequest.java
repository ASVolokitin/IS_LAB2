package com.ticket_is.app.dto.request;

import com.ticket_is.app.model.Location;
import com.ticket_is.app.model.enums.Color;
import com.ticket_is.app.model.enums.Country;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PersonRequest(

    @NotNull
    String eyeColor,

    @NotNull
    String hairColor,

    @Valid
    Location location,

    @NotBlank
    @Size(max=29)
    String passportID,

    @Valid
    Country nationality

) {
    public Color getEyeColor() {
        return Color.valueOf(eyeColor);
    }

    public Color getHairColor() {
        return Color.valueOf(hairColor);
    }
}
