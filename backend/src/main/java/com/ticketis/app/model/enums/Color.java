package com.ticketis.app.model.enums;

import java.util.Arrays;

public enum Color {
    GREEN,
    BLUE,
    YELLOW,
    WHITE;

    public static String[] getNames() {
        return Arrays.stream(Color.values())
            .map(Enum::name)
            .toArray(String[]::new);
    }
}