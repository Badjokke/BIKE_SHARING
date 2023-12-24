package com.example.bike_sharing_location.service.bike;

import com.example.bike_sharing_location.configuration.BikeServiceConfiguration;
import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.BikeRepository;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;
import com.example.bike_sharing_location.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * implementation of bike service logic
 * for detailed method description check interface file
 */
@Service
public class BikeServiceImpl implements BikeService{
    private final Logger logger = Logger.getLogger(BikeServiceImpl.class.getName());

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
        initBikeLocationTask();
        this.userService = userService;
    }
    @Override
    public List<Bike> getBikesDueForService() {
        logger.info("Querying all bikes due for service");
        return this.bikeRepository.fetchAllBikesDueForService(this.bikeServiceInterval);
    }

    @Override
    public List<Bike> getRideableBikes() {
        logger.info("Querying all rideable bikes");

        return this.bikeRepository.fetchAllBikesRideableBikes(this.bikeServiceInterval);
    }

    @Override
    public List<Bike> getAllBikes() {
        logger.info("Querying all bikes");
        return this.bikeRepository.findAll();
    }

    @Override
    public List<Bike> getAllCurrentBikes() {
        logger.info("Returning all bikes currently in memory storage");

        List<Bike> bikes = this.bikeStorage.getAllBikes();
        return bikes;
    }

    @Override
    public Bike getCurrentBike(Long bikeId) {
        logger.info("Returning bike with id: "+bikeId+" from memory storage");
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
            //this log could introduce performance issues at scale - io is slow
            logger.info("Updating location of bike: "+b.getId()+" to location -  long: "+b.getLongitude()+"lat: "+b.getLatitude());
            bikesUpdated += this.bikeRepository.updateBikeLocation(b.getId(),b.getLongitude(),b.getLatitude());
        }
        return bikesUpdated == batchSize;

    }

    @Override
    public int updateBikeStand(long bikeId, long standId) {
        logger.info("Updating stand id bike with id: "+bikeId+" to: "+standId);
        return this.bikeRepository.updateBikeStand(bikeId,standId);
    }

    @Override
    @Transactional
    public boolean markBikeAsServiced(long bikeId, String userEmail, String token) {
        User user = this.userService.fetchUserInfo(userEmail,token);
        if (user == null || user.getRole() == User.RoleEnum.REGULAR){
            logger.warning("Failed to mark bike with id: "+bikeId+" as marked by user: "+userEmail+". User token: "+token);
            return false;
        }
        logger.info("Marked bike with id: "+bikeId+ " as serviced.");
        int updatedBikes = this.bikeRepository.updateBikeServiceTime(bikeId);
        return updatedBikes == 1;
    }


    @Override
    public InMemoryBikeStorage getInMemoryBikeStorage() {
        return this.bikeStorage;
    }
    //necessary for claimBike to be monitor method - its basically a tsl instruction
    @Override
    public synchronized int claimBike(Bike bike) {
        if(bike == null){
            logger.severe("Bike reference is null in claimBike method.");
            return 0;
        }

        if(bike.getId() == 0 || this.bikeStorage.isBikeUsed(bike)){
            logger.warning("Failed to  claim bike with id: "+bike.getId()+".");
            return 0;
        }
        this.bikeStorage.useBike(bike);
        logger.info("Claimed bike with id: "+bike.getId()+" from stand.");
        return this.bikeRepository.removeBikeFromStand(bike.getId());
    }

    @Override
    public void releaseBike(Bike bike) {
        logger.info("Releasing bike with id: "+bike.getId()+ " from user ride");
        this.bikeStorage.releaseBike(bike);
    }


    private void initStorage(){
        List<Bike> bikes = this.getAllBikes();
        logger.info("Initializing bike memory storage with: "+bikes.size()+" bikes.");
        this.bikeStorage.fillMemoryStorage(bikes);
    }

    /**
     *task that writes current location of bikes from memory storage to database once in a "while" - while is configurable in app.properties
     */
    private void initBikeLocationTask(){

        Runnable updateBikeLocationTask = () -> {
            List<Bike> affectedBikes = this.bikeStorage.getModifiedBikes();
            boolean updateOkay = updateBikesLocation(affectedBikes);
            if(!updateOkay){
                logger.severe("Failed to update bike locations from memory storage to database.");
            }
            int n = affectedBikes.size();
            if(n > 0) {
                logger.info("updated locations of: " + n + " bikes.");
                this.bikeStorage.clearModifiedBikes();
            }
        };
        logger.info("Scheduling db write task for bikes with interval :"+bikeUpdateInterval+" seconds.");
        executorService.scheduleAtFixedRate(updateBikeLocationTask,bikeUpdateInterval,bikeUpdateInterval,TimeUnit.SECONDS);

    }
}
