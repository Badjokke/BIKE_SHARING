package com.example.bike_sharing_location.model;

public class StompBikeLocationMessageWrapper {
    private long bikeId;

    public StompBikeLocationMessageWrapper(long bikeId, Location location) {
        this.bikeId = bikeId;
    }


    public StompBikeLocationMessageWrapper() {
    }

    public long getBikeId() {
        return bikeId;
    }

    public void setBikeId(long bikeId) {
        this.bikeId = bikeId;
    }
}
