package com.example.bike_sharing_location.repository;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.Location;
import org.springframework.context.annotation.Configuration;

import java.util.*;

public class InMemoryBikeStorage {
    private final Map<Long, Bike> bikeMemoryStorage;
    private final Set<Bike> modifiedBikes;
    private final Set<Bike> usedBikes;
    private final List<Bike> bikes;
    public InMemoryBikeStorage(){
        this.bikeMemoryStorage = new HashMap<>();
        this.modifiedBikes = new HashSet<>();
        this.usedBikes = new HashSet<>();
        this.bikes = new ArrayList<>();
    }
    public List<Bike> getModifiedBikes(){
        return new ArrayList<>(this.modifiedBikes);
    }
    public List<Bike> getAllBikes(){
        return this.bikes;
    }
    public Bike getBike(Long bikeId){
        return this.bikeMemoryStorage.get(bikeId);
    }
    public boolean isBikeUsed(Bike bike){
        return this.usedBikes.contains(bike);
    }
    public void removeBike(Bike bike){
        this.usedBikes.remove(bike);
    }
    public void useBike(Bike bike){
        this.usedBikes.add(bike);
    }
    public void releaseBike(Bike bike){
        this.usedBikes.remove(bike);
    }
    public void clearModifiedBikes(){
        this.modifiedBikes.clear();
    }
    public void fillMemoryStorage(List<Bike> bikes){
        if(bikes == null || bikes.size() == 0){
            System.out.println("No bikes provided to memory storage.");
            return;
        }
        for(Bike b : bikes){
            addBikeToStorage(b);
        }
    }
    public void addBikeToStorage(Bike b){
        this.bikes.add(b);
        this.bikeMemoryStorage.put(b.getId(),b);
    }
    private void markBikeAsModified(Bike bike){
        this.modifiedBikes.add(bike);
    }
    public Bike updateBikeLocation(long bikeId, Location location){
        Bike bike = this.bikeMemoryStorage.get(bikeId);
        if(bike == null){
            throw new RuntimeException("Invalid bikeId provided");
        }
        bike.setLatitude(location.getLatitude());
        bike.setLongitude(location.getLongitude());
        markBikeAsModified(bike);
        return bike;
    }


}
