package com.example.bike_sharing_location.model;

public class BikeDto {
    private final long id;
    private final String name;
    private final double x;
    private final double y;

    public BikeDto(long id, String name, double x, double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
