package com.example.bike_sharing_location.repository;


import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.Location;


import java.util.*;
import java.util.logging.Logger;

/**
 * Implementation of in memory storage for bikes
 * used for real time data streaming of bikes
 */
public class InMemoryBikeStorage {
    //storage that maps id of a bike to bike object
    private final Map<Long, Bike> bikeMemoryStorage;
    //bikes that are being ridden by users - bikes in this set will be updated in database after timeout
    private final Set<Bike> modifiedBikes;
    //bikes that are currently ridden therefore are not available
    private final Set<Bike> usedBikes;
    //all available bikes
    private final List<Bike> bikes;
    private final Logger logger = Logger.getLogger(InMemoryBikeStorage.class.getName());

    public InMemoryBikeStorage(){
        logger.info("Creating in memory bike storage.");
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
            logger.severe("No bikes provided to memory storage.");
            return;
        }
        logger.info("Adding bikes to in memory storage: "+bikes);
        for(Bike b : bikes){
            addBikeToStorage(b);
        }
    }
    public void addBikeToStorage(Bike b){
        logger.info("adding bike "+b+" to storage.");
        this.bikes.add(b);
        this.bikeMemoryStorage.put(b.getId(),b);
    }
    private void markBikeAsModified(Bike bike){
        logger.info(bike.getId()+" marked as modified.");
        this.modifiedBikes.add(bike);
    }
    public Bike updateBikeLocation(long bikeId, Location location){
        Bike bike = this.bikeMemoryStorage.get(bikeId);
        if(bike == null){
            logger.severe("Update of location: "+bikeId+" invalid. Bike doesnt exist in storage.");
            return null;
        }
        logger.info("Updating location of bike: "+bikeId+" to location: "+location);
        bike.setLatitude(location.getLatitude());
        bike.setLongitude(location.getLongitude());
        markBikeAsModified(bike);
        return bike;
    }


}
