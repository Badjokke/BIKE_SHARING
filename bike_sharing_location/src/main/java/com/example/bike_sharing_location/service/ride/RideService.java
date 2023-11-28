package com.example.bike_sharing_location.service.ride;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Ride;
import com.example.bike_sharing_location.model.BikeRide;
import com.example.bike_sharing_location.model.Location;
import com.example.bike_sharing_location.model.RideStart;

import java.util.List;

public interface RideService {
    List<Ride> fetchUserRides(long userId);
    BikeRide createUserRide(RideStart rideWrapper);
    boolean rideExists(String rideToken);
    boolean rideFinished(String rideToken);
    Bike updateLocation(String rideToken, Location bikeLocation);

}
