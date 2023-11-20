package com.example.bike_sharing_location.repository;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.Location;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class InMemoryBikeStorage {
    private final Map<Long, Bike> bikeMemoryStorage;
    private final Set<Long> modifiedBikes;
    public InMemoryBikeStorage(){
        this.bikeMemoryStorage = new HashMap<>();
        this.modifiedBikes = new HashSet<>();
    }
    public List<Long> getModifiedBikes(){
        return new ArrayList<>(this.modifiedBikes);
    }
    public void clearModifiedBikes(){
        this.modifiedBikes.clear();
    }
    public void fillMemoryStorage(List<Bike> bikes){
        if(bikes == null || bikes.size() == 0){
            throw new RuntimeException("No bikes provided to memory storage.");
        }
        for(Bike b : bikes){
            addBikeToStorage(b);
        }
    }
    public void addBikeToStorage(Bike b){
        this.bikeMemoryStorage.put(b.getId(),b);
    }
    public void markBikeAsModified(long bikeId){
        this.modifiedBikes.add(bikeId);
    }
    public void updateBikeLocation(long bikeId, Location location){
        Bike bike = this.bikeMemoryStorage.get(bikeId);
        if(bike == null){
            throw new RuntimeException("Invalid bikeId provided");
        }
        bike.setLatitude(location.latitude());
        bike.setLongitude(location.longitude());
        markBikeAsModified(bike.getId());
    }


}
