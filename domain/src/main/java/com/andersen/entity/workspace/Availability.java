package com.andersen.entity.workspace;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents the availability of a workspace for a specific date and time.
 * This class keeps track of the total capacity and the remaining available spots.
 */
public class Availability {
    private long id ;
    private LocalDate date;
    private LocalTime time;
    private int capacity;
    private int remaining;

    /**
     * Default constructor for Availability.
     */
    public Availability() {
    }

    /**
     * Constructs a new Availability with the specified date, time, capacity, and remaining spots.
     *
     * @param date     the date of availability
     * @param time     the time of availability
     * @param capacity the total capacity available at this date and time
     * @param remaining the number of remaining spots available (initialized to capacity)
     */
    public Availability(long id,LocalDate date, LocalTime time, int capacity, int remaining) {
       this.id=id;
        this.date = date;
        this.time = time;
        this.capacity = capacity;
        this.remaining = capacity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the date of availability.
     *
     * @return the availability date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the time of availability.
     *
     * @return the availability time
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Gets the total capacity for this availability.
     *
     * @return the total capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the number of remaining spots available.
     *
     * @return the number of remaining spots
     */
    public int getRemaining() {
        return remaining;
    }


}