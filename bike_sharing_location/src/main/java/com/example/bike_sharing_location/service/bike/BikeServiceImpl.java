package com.example.bike_sharing_location.service.bike;

import com.example.bike_sharing_location.configuration.BikeServiceConfiguration;
import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.Location;
import com.example.bike_sharing_location.repository.BikeRepository;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeServiceImpl implements BikeService{
    private final BikeServiceConfiguration bikeServiceConfiguration;
    private final BikeRepository bikeRepository;
    private final double bikeServiceInterval;

    private final InMemoryBikeStorage bikeStorage;

    public BikeServiceImpl(BikeServiceConfiguration bikeServiceConfiguration, BikeRepository bikeRepository, InMemoryBikeStorage bikeStorage){
        this.bikeRepository = bikeRepository;
        this.bikeServiceConfiguration = bikeServiceConfiguration;
        this.bikeServiceInterval = this.bikeServiceConfiguration.getBIKE_SERVICE_INTERVAL();
        this.bikeStorage = bikeStorage;

    }
    @Override
    public List<Bike> getBikesDueForService() {
        return this.bikeRepository.fetchAllBikesDueForService(this.bikeServiceInterval);
    }

    @Override
    public List<Bike> getRideableBikes() {
        return this.bikeRepository.fetchAllBikesNotDueForService(this.bikeServiceInterval);
    }

    @Override
    public List<Bike> getAllBikes() {
        return this.bikeRepository.findAll();
    }

    @Override
    @Transactional
    public boolean updateBikeLocation(long bikeId, Location location) {
        int updatedBikes = this.bikeRepository.updateBikeLocation(bikeId,location);
        return updatedBikes == 1;
    }

    @Override
    @Transactional
    public boolean updateBikesLocation(List<Long> bikeIds, List<Location> locations) {
        int batchSize = bikeIds.size();
        if(batchSize != locations.size()){
            return false;
        }
        int bikesUpdated = 0;
        for(int i = 0; i < batchSize; i++){
            bikesUpdated += this.bikeRepository.updateBikeLocation(bikeIds.get(i),locations.get(i));
        }
        System.out.println(bikesUpdated);
        return batchSize == bikesUpdated;
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


}
