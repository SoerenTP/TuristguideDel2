package com.example.turistguidedel2.model;

public enum Tags {
    MUSEUM("Museum"),
    FREE("Free"),
    PAID("Paid"),
    FAMILY_FRIENDLY("Family Friendly"),
    HISTORICAL("Historical"),
    RELIGION("Religion"),
    AMUSEMENT_PARK("Amusement Park");

    private final String name;

    Tags(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}