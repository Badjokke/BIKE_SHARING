package com.example.bike_sharing_location.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public class BikeRide {
    private Long userId;

    private Long bikeId;

    private Long startStandId;

    private Long endStandId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime rideStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime rideEnd;

    public BikeRide() {
    }

    public BikeRide(Long userId, Long bikeId, Long startStandId, Long endStandId, OffsetDateTime rideStart) {
        this.userId = userId;
        this.bikeId = bikeId;
        this.startStandId = startStandId;
        this.endStandId = endStandId;
        this.rideStart = rideStart;
        this.rideEnd = null;
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
}
