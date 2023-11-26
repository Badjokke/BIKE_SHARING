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
    boolean updateBikeLocation(long bikeId,Location location);
    boolean updateBikesLocation(List<Bike> bikes);

    boolean markBikeAsServiced(long bikeId);

    boolean markBikesAsServiced(List<Long> bikeIds);
    InMemoryBikeStorage getInMemoryBikeStorage();

    boolean isBikeRideable(Bike bike);
     void claimBike(Bike bike);
}
