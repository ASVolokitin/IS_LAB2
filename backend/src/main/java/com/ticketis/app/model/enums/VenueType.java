package com.ticketis.app.model.enums;

import java.util.Arrays;

public enum VenueType {
    PUB,
    LOFT,
    OPEN_AREA,
    MALL;

    public static String[] getNames() {
        return Arrays.stream(VenueType.values())
            .map(Enum::name)
            .toArray(String[]::new);
    }
}