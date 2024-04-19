package com.example.bike_sharing_location.model;

public class StompBikeRideMessageWrapper {
    private String rideToken;
    private Location location;

    public StompBikeRideMessageWrapper(String rideToken, Location location) {
        this.rideToken = rideToken;
        this.location = location;
    }


    public StompBikeRideMessageWrapper() {
    }

    public String getRideToken() {
        return rideToken;
    }

    public Location getLocation(){
        return this.location;
    }

    public void setBikeId(String rideToken) {
        this.rideToken = rideToken;
    }
    public void setLocation(Location location){
        this.location = location;
    }
}
