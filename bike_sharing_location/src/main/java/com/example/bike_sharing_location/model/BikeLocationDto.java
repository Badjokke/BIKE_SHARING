package com.example.bike_sharing_location.model;

public class BikeLocationDto {
    private double x;
    private double y;
    private long id;

    public BikeLocationDto(double x, double y, long id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public long getId() {
        return id;
    }
}
