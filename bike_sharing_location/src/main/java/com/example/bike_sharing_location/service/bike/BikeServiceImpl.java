package com.example.bike_sharing_location.service.bike;

import com.example.bike_sharing_location.configuration.BikeServiceConfiguration;
import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.Location;
import com.example.bike_sharing_location.repository.BikeRepository;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class BikeServiceImpl implements BikeService{
    private final BikeRepository bikeRepository;
    private final double bikeServiceInterval;
    private final long bikeUpdateInterval;
    private final ScheduledExecutorService executorService;
    private final InMemoryBikeStorage bikeStorage;

    public BikeServiceImpl(BikeServiceConfiguration bikeServiceConfiguration, BikeRepository bikeRepository){
        this.bikeRepository = bikeRepository;
        this.bikeServiceInterval = bikeServiceConfiguration.getBIKE_SERVICE_INTERVAL();
        this.bikeUpdateInterval = bikeServiceConfiguration.getBIKE_SERVICE_UPDATE_INTERVAL();
        this.bikeStorage = new InMemoryBikeStorage();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        initStorage();
        initBikeLocationThread();
    }
    @Override
    public List<Bike> getBikesDueForService() {
        return this.bikeRepository.fetchAllBikesDueForService(this.bikeServiceInterval);
    }

    @Override
    public List<Bike> getRideableBikes() {
        return this.bikeRepository.fetchAllBikesRideableBikes(this.bikeServiceInterval);
    }

    @Override
    public List<Bike> getAllBikes() {
        return this.bikeRepository.findAll();
    }

    @Override
    public List<Bike> getAllCurrentBikes() {
        List<Bike> bikes = this.bikeStorage.getAllBikes();
        return bikes;
    }

    @Override
    public Bike getCurrentBike(Long bikeId) {
        return this.bikeStorage.getBike(bikeId);
    }

    @Override
    @Transactional
    public boolean updateBikeLocation(long bikeId, Location location) {
        int updatedBikes = this.bikeRepository.updateBikeLocation(bikeId, location.getLongitude(), location.getLatitude());
        return updatedBikes == 1;
    }

    @Override
    public boolean updateBikesLocation(List<Bike> bikes) {
        int batchSize = bikes.size();
        if(batchSize == 0){
            return true;
        }
        int bikesUpdated = 0;
        for(Bike b : bikes){
            bikesUpdated += this.bikeRepository.updateBikeLocation(b.getId(),b.getLongitude(),b.getLatitude());
        }
        return bikesUpdated == batchSize;

    }

    @Override
    @Transactional
    public boolean markBikeAsServiced(long bikeId) {
        int updatedBikes = this.bikeRepository.updateBikeServiceTime(bikeId);
        return updatedBikes == 1;
    }

    @Override
    @Transactional
    public boolean markBikesAsServiced(List<Long> bikeIds) {
        int bikeCount = bikeIds.size();
        int updatedBikes = this.bikeRepository.updateBikesServiceTime(bikeIds);
        return updatedBikes == bikeCount;
    }

    @Override
    public InMemoryBikeStorage getInMemoryBikeStorage() {
        return this.bikeStorage;
    }

    @Override
    public synchronized boolean isBikeRideable(Bike bike) {
        return this.bikeStorage.isBikeUsed(bike);
    }

    @Override
    public synchronized int claimBike(Bike bike) {
        if(bike == null || bike.getId() == 0 || this.bikeStorage.isBikeUsed(bike)){
            return 0;
        }
        this.bikeStorage.useBike(bike);
        return this.bikeRepository.removeBikeFromStand(bike.getId());
    }


    private void initStorage(){
        List<Bike> bikes = this.getRideableBikes();
        this.bikeStorage.fillMemoryStorage(bikes);
    }

    private void initBikeLocationThread(){

        Runnable updateBikeLocationTask = () -> {
            List<Bike> affectedBikes = this.bikeStorage.getModifiedBikes();
            boolean updateOkay = updateBikesLocation(affectedBikes);
            if(!updateOkay){
                System.out.println("bike location update failed");
            }
            System.out.println("updated locations of: "+affectedBikes.size()+" bikes.");
        };
        executorService.scheduleAtFixedRate(updateBikeLocationTask,bikeUpdateInterval,bikeUpdateInterval,TimeUnit.SECONDS);

    }
}
