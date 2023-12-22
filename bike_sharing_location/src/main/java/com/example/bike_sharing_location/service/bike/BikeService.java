package com.example.bike_sharing_location.service.bike;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.Location;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;

import java.util.List;

public interface BikeService {
    List<Bike> getBikesDueForService();
    List<Bike> getRideableBikes();
    List<Bike> getAllBikes();

    List<Bike> getAllCurrentBikes();
    Bike getCurrentBike(Long bikeId);
    boolean updateBikesLocation(List<Bike> bikes);
    int updateBikeStand(long bikeId, long standId);
    boolean markBikeAsServiced(long bikeId, String email, String token);

    InMemoryBikeStorage getInMemoryBikeStorage();

     int claimBike(Bike bike);
     void releaseBike(Bike bike);
}
