package com.example.bike_sharing_location.service.bike;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;

import java.util.List;

/**
 * interface for business logic regarding bikes
 */
public interface BikeService {
    /**
     * Method fetches all bikes due for service from database
     * @return List of all bikes due for service
     */
    List<Bike> getBikesDueForService();

    /**
     * Method fetches all bikes that are rideable from database - i.e those that are in stand (not being used)
     * and are not due for service
     * @return List of all rideable bikes
     */
    List<Bike> getRideableBikes();

    /**
     * Fetches all bikes from database
     * for actual location of bikes use getAllCurrentBikes, database location information is not consistent
     * @return List of all bikes currently in database
     */
    List<Bike> getAllBikes();

    /**
     * Fetches all bikes from memory storage
     * @return List of all bikes that are rideable or being ridden
     */
    List<Bike> getAllCurrentBikes();

    /**
     * Returns bike from memory storage with given id
     * @param bikeId id of bike
     * @return Bike if bike with given id exists, null otherwise
     */
    Bike getCurrentBike(Long bikeId);

    /**
     * Updates bike location in database from memory storage
     * @param bikes List of bikes whose location will be written to db
     * @return true if all updated rows = bikes.size(), false otherwise
     */
    boolean updateBikesLocation(List<Bike> bikes);

    /**
     * Method sets stand of bike @param bikeId to stand @param standId
     * @param bikeId id of bike whose row will be updated
     * @param standId id of stand that will be set as bikes stand
     * @return number of updated rows - should always be 1
     */
    int updateBikeStand(long bikeId, long standId);

    /**
     * Method marks bike as serviced with current timestamp (Date.now())
     * @param bikeId id of bike set as serviced
     * @param email email of user who is updating the bike - used for fetching userInfo from user microservice
     * @param token jwt token of user
     * @return true if bike is marked as serviced, false otherwise
     */
    boolean markBikeAsServiced(long bikeId, String email, String token);

    /**
     * Getter for memory storage - stores
     * @return in memory storage instance
     */
    InMemoryBikeStorage getInMemoryBikeStorage();

    /**
     * Claims bike from storage - marks it as "up for ride" so other users cant take it
     * updates row in database - sets standId to null
     * @param bike bike that is requested by user
     * @return number of affected rows - should always be 1
     */
     int claimBike(Bike bike);

    /**
     * marks bike as "usable" for other users
     * @param bike bike that is released after ride
     */
     void releaseBike(Bike bike);
}
