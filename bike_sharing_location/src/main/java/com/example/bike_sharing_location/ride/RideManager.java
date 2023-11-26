package com.example.bike_sharing_location.ride;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.model.BikeRide;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;
import com.example.bike_sharing_location.repository.InMemoryStandStorage;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class RideManager {
    private final InMemoryBikeStorage bikeStorage;
    private final InMemoryStandStorage standStorage;
    private final Map<Long, BikeRide> rides;
    public RideManager(InMemoryBikeStorage bikeStorage, InMemoryStandStorage standStorage){
        this.bikeStorage = bikeStorage;
        this.standStorage = standStorage;
        this.rides = new HashMap<>();
    }


   public BikeRide startRide(Bike bike, User user, Stand startStand, Stand endDate){
       OffsetDateTime now = OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC);
       BikeRide newRide = new BikeRide(user.getId(),bike.getId(),startStand.getId(),endDate.getId(),now);
       this.rides.put(bike.getId(),newRide);
       return newRide;
   }
   public void endRide(Bike bike){
        this.rides.remove(bike.getId());
   }









}
