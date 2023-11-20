package com.example.bike_sharing_location.service.bike;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.Location;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;

import java.util.List;

public interface BikeService {
    List<Bike> getBikesDueForService();
    List<Bike> getRideableBikes();
    List<Bike> getAllBikes();

    boolean updateBikeLocation(long bikeId,Location location);
    boolean updateBikesLocation(List<Long> bikeIds, List<Location> locations);

    boolean markBikeAsServiced(long bikeId);

    boolean markBikesAsServiced(List<Long> bikeIds);
    InMemoryBikeStorage getInMemoryBikeStorage();
}
