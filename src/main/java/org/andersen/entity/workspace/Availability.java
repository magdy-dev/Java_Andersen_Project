package org.andersen.entity.workspace;

import java.time.LocalDate;
import java.time.LocalTime;


public class Availability {
    private final LocalDate date;
    private final LocalTime time;
    private final int capacity;
    private int remaining;

    public Availability(LocalDate date, LocalTime time, int capacity) {
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
