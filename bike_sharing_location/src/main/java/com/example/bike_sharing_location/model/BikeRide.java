package com.example.bike_sharing_location.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/**
 * DTO for BikeRide
 */
public class BikeRide {
    //user who is riding the bike
    private Long userId;
    //bike being ridden
    private Long bikeId;
    //from which stand the ride was started
    private Long startStandId;
    //ending stand of the ride (the goal)
    private Long endStandId;
    //generated token for ride - identifies ride
    private String rideToken;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime rideStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime rideEnd;

    public BikeRide() {
    }

    public BikeRide(Long userId, Long bikeId, Long startStandId, Long endStandId, OffsetDateTime rideStart, String token) {
        this.userId = userId;
        this.bikeId = bikeId;
        this.startStandId = startStandId;
        this.endStandId = endStandId;
        this.rideStart = rideStart;
        this.rideEnd = null;
        this.rideToken = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBikeId() {
        return bikeId;
    }

    public void setBikeId(Long bikeId) {
        this.bikeId = bikeId;
    }

    public Long getStartStandId() {
        return startStandId;
    }

    public void setStartStandId(Long startStandId) {
        this.startStandId = startStandId;
    }

    public Long getEndStandId() {
        return endStandId;
    }

    public void setEndStandId(Long endStandId) {
        this.endStandId = endStandId;
    }

    public OffsetDateTime getRideStart() {
        return rideStart;
    }

    public void setRideStart(OffsetDateTime rideStart) {
        this.rideStart = rideStart;
    }

    public OffsetDateTime getRideEnd() {
        return rideEnd;
    }

    public void setRideEnd(OffsetDateTime rideEnd) {
        this.rideEnd = rideEnd;
    }
    public String getRideToken(){return this.rideToken;}
}
