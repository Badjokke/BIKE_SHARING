package com.example.bike_sharing_location.model;

public class StompBikeLocationWrapper {
    private long bikeId;
    private Location location;

    public StompBikeLocationWrapper(long bikeId, Location location) {
        this.bikeId = bikeId;
        this.location = location;
    }

    public StompBikeLocationWrapper(long bikeId) {
        this.bikeId = bikeId;
    }

    public StompBikeLocationWrapper() {
    }

    public long getBikeId() {
        return bikeId;
    }

    public Location getLocation(){
        return this.location;
    }

    public void setBikeId(long bikeId) {
        this.bikeId = bikeId;
    }
    public void setLocation(Location location){
        this.location = location;
    }
}
