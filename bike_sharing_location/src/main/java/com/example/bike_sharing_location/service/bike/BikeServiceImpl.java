package com.example.bike_sharing_location.service.bike;

import com.example.bike_sharing_location.configuration.BikeServiceConfiguration;
import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.Location;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.BikeRepository;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;
import com.example.bike_sharing_location.service.user.UserService;
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
    private final UserService userService;

    public BikeServiceImpl(BikeServiceConfiguration bikeServiceConfiguration, BikeRepository bikeRepository, UserService userService){
        this.bikeRepository = bikeRepository;
        this.bikeServiceInterval = bikeServiceConfiguration.getBIKE_SERVICE_INTERVAL();
        this.bikeUpdateInterval = bikeServiceConfiguration.getBIKE_SERVICE_UPDATE_INTERVAL();
        this.bikeStorage = new InMemoryBikeStorage();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        initStorage();
        initBikeLocationThread();
        this.userService = userService;
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
    public int updateBikeStand(long bikeId, long standId) {
        return this.bikeRepository.updateBikeStand(bikeId,standId);
    }

    @Override
    @Transactional
    public boolean markBikeAsServiced(long bikeId, String userEmail, String token) {
        User user = this.userService.fetchUserInfo(userEmail,token);
        if (user == null || user.getRole() == User.RoleEnum.SERVICEMAN)
            return false;
        int updatedBikes = this.bikeRepository.updateBikeServiceTime(bikeId);
        return updatedBikes == 1;
    }


    @Override
    public InMemoryBikeStorage getInMemoryBikeStorage() {
        return this.bikeStorage;
    }

    @Override
    public synchronized int claimBike(Bike bike) {
        if(bike == null || bike.getId() == 0 || this.bikeStorage.isBikeUsed(bike)){
            return 0;
        }
        this.bikeStorage.useBike(bike);
        return this.bikeRepository.removeBikeFromStand(bike.getId());
    }

    @Override
    public void releaseBike(Bike bike) {
        this.bikeStorage.releaseBike(bike);
    }


    private void initStorage(){
        List<Bike> bikes = this.getAllBikes();
        this.bikeStorage.fillMemoryStorage(bikes);
    }

    private void initBikeLocationThread(){

        Runnable updateBikeLocationTask = () -> {
            List<Bike> affectedBikes = this.bikeStorage.getModifiedBikes();
            boolean updateOkay = updateBikesLocation(affectedBikes);
            if(!updateOkay){
                System.out.println("bike location update failed");
            }
            int n = affectedBikes.size();
            if(n > 0) {
                System.out.println("updated locations of: " + n + " bikes.");
                this.bikeStorage.clearModifiedBikes();
            }
        };
        executorService.scheduleAtFixedRate(updateBikeLocationTask,bikeUpdateInterval,bikeUpdateInterval,TimeUnit.SECONDS);

    }
}
