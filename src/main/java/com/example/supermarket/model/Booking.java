package com.example.supermarket.model;

import lombok.Data;

import java.time.DayOfWeek;
import java.util.UUID;

@Data
public class Booking {

    private UUID id = UUID.randomUUID();
    private int weekday;
    private String weekdayName;
    private int slot;
    private User user;

    public Booking(int weekday, int slot, User user) {
        this.weekday = weekday;
        this.slot = slot;
        this.user = user;
        this.weekdayName = DayOfWeek.of(weekday).name();
    }

}
