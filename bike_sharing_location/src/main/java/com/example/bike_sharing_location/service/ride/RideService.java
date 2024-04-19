package com.example.bike_sharing_location.service.ride;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Ride;
import com.example.bike_sharing_location.model.BikeRide;
import com.example.bike_sharing_location.model.Location;
import com.example.bike_sharing_location.model.RideStart;

import java.util.List;

/**
 * interface for bike ride logic
 */
public interface RideService {
    /**
     * Fetches all completed user rides
     * @param userId id of user whose rides will be fetched
     * @return List of all user rides
     */
    List<Ride> fetchUserRides(long userId);

    /**
     * Creates user ride object and stores it in memory until ride is finished
     * @param rideWrapper DTO for user ride request - contains bikeId, starting stand id (where was bike parked), end stand id (goal of the ride)
     * @param authorization user authorization token - jwt or google
     * @return BikeRide dto response for user - contains generated ride token
     */
    BikeRide createUserRide(RideStart rideWrapper, String authorization);

    /**
     * Method checks if ride exists - if it doesnt then the token provided as @param is invalid
     * @param rideToken token identifying user ride
     * @return true if ride identified by token exists
     */
    boolean rideExists(String rideToken);

    /**
     * Method checks if ride is finish - i.e. if bike is <= <min_bike_distance> from end stand
     * @param rideToken token identifying ride
     * @return true if bike is close enough to stand
     */
    boolean rideFinished(String rideToken);
    Bike updateLocation(String rideToken, Location bikeLocation);

    /**
     * Saves user ride to database once the ride is finished
     * @param rideToken token identifying ride
     * @return Created row in database
     */
    Ride saveRide(String rideToken);
}
