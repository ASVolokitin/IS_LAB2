package com.ticketis.app.model.enums;

import java.util.Arrays;

public enum TicketType {
    VIP,
    USUAL,
    BUDGETARY,
    CHEAP;

    public static String[] getNames() {
        return Arrays.stream(TicketType.values())
            .map(Enum::name)
            .toArray(String[]::new);
    }
}