package com.ticketly.model;

import java.time.LocalDateTime;

public class Theater {
    private long id;
    private String name;
    private String location;
    private int totalSeats;
    private LocalDateTime createdAt;

    // Constructors
    public Theater() {}

    public Theater(long id, String name, String location, int totalSeats, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalSeats = totalSeats;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Theater{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", totalSeats=" + totalSeats +
                ", createdAt=" + createdAt +
                '}';
    }
}
