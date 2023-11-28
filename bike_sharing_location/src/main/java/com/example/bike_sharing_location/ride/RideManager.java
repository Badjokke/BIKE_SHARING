package com.example.bike_sharing_location.ride;

import com.example.bike_sharing_location.crypto.DefaultEncryption;
import com.example.bike_sharing_location.crypto.EncryptionEngine;
import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.model.BikeRide;
import com.example.bike_sharing_location.model.Location;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;
import com.example.bike_sharing_location.repository.InMemoryStandStorage;
import com.example.bike_sharing_location.utils.BikeStandDistance;
import com.example.bike_sharing_location.utils.LinAlg;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class RideManager {
    private final InMemoryBikeStorage bikeStorage;
    private final InMemoryStandStorage standStorage;
    private final Map<String, BikeRide> rides;
    private final LinAlg algebra;
    private final double minDistance;
    public RideManager(InMemoryBikeStorage bikeStorage, InMemoryStandStorage standStorage, double minDistance){
        this.bikeStorage = bikeStorage;
        this.standStorage = standStorage;
        this.rides = new HashMap<>();
        this.algebra = new BikeStandDistance();
        this.minDistance = minDistance;
    }


   public BikeRide startRide(Bike bike, User user, Stand startStand, Stand endDate){
       OffsetDateTime now = OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC);
       String rideToken = this.createRideToken(user,bike);
       BikeRide newRide = new BikeRide(user.getId(),bike.getId(),startStand.getId(),endDate.getId(),now,rideToken);
       this.rides.put(rideToken,newRide);
       return newRide;
   }
   public void endRide(String rideToken){
        this.rides.remove(rideToken);
   }

   private String createRideToken(User rideUser, Bike bike){
       EncryptionEngine engine = new DefaultEncryption();
       String hash = engine.generateHash(rideUser.getEmail()+rideUser.getId()+rideUser.getUsername()+bike.getId());
       return hash;
   }
   public BikeRide getRide(String token){
        return this.rides.get(token);
   }

   public Bike updateBikeLocation(String token, Location location){
        BikeRide ride = this.rides.get(token);
        if(ride == null){
            return null;
        }
       return this.bikeStorage.updateBikeLocation(ride.getBikeId(),location);
   }

   public boolean isRideFinished(String token){
        BikeRide ride = this.rides.get(token);
        if(ride == null){
            return false;
        }
        Bike b = this.bikeStorage.getBike(ride.getBikeId());
        Stand s = this.standStorage.getStand(ride.getEndStandId());
        double bikeToStandDistance = this.algebra.computeDistance(b,s);
        return bikeToStandDistance <= this.minDistance;
   }







}
