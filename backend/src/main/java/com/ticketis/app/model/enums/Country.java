package com.ticketis.app.model.enums;

import java.util.Arrays;

public enum Country {
    RUSSIA,
    USA,
    CHINA,
    ITALY;

    public static String[] getNames() {
        return Arrays.stream(Country.values())
            .map(Enum::name)
            .toArray(String[]::new);
    }
}