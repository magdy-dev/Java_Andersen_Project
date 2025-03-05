package com.andersen.entity.workspace;

import java.time.LocalDate;
import java.time.LocalTime;

public class Availability {
    private LocalDate date;
    private LocalTime time;
    private int capacity;
    private int remaining;

    public Availability() {
    }
    public Availability(LocalDate date, LocalTime time, int capacity, int remaining) {
        this.date = date;
        this.time = time;
        this.capacity = capacity;
        this.remaining = capacity;

    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRemaining() {
        return remaining;
    }

    public void decrement() {
        if (remaining > 0) remaining--;
    }

    public void increment() {
        if (remaining < capacity) remaining++;
    }
}
