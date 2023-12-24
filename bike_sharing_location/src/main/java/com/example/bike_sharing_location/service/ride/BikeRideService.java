package com.example.bike_sharing_location.service.ride;

import com.example.bike_sharing_location.configuration.RideServiceConfiguration;
import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Ride;
import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.model.BikeRide;
import com.example.bike_sharing_location.model.Location;
import com.example.bike_sharing_location.model.RideStart;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.RideRepository;
import com.example.bike_sharing_location.ride.RideManager;
import com.example.bike_sharing_location.service.bike.BikeService;
import com.example.bike_sharing_location.service.stand.StandService;
import com.example.bike_sharing_location.service.user.UserService;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of RideService interface - check interface for method description
 */
@Service
public class BikeRideService implements RideService{
    private final RideRepository rideRepository;
    private final BikeService bikeService;
    private final StandService standService;
    private final RideManager rideManager;
    private final UserService userService;
    private final Logger logger = Logger.getLogger(BikeRideService.class.getName());

    public BikeRideService(RideRepository rideRepository, UserService userService, BikeService bikeService, StandService standService, RideServiceConfiguration rideConfiguration){
        this.rideRepository = rideRepository;
        this.userService = userService;
        this.bikeService = bikeService;
        this.standService = standService;
        this.rideManager = new RideManager(this.bikeService.getInMemoryBikeStorage(),this.standService.getInMemoryStandStorage(),rideConfiguration.getBikeMinDistance());
    }
    @Override
    public List<Ride> fetchUserRides(long userId) {
        logger.info("Fetching user rides of user: "+userId);
        return this.rideRepository.fetchUserRides(userId);
    }

    @Override
    public BikeRide createUserRide(RideStart rideStart,String authorization) {
        String userEmail = rideStart.getUserEmail();
        logger.info("Creating user ride for user: "+userEmail);
        User userInfo = this.fetchUserInfo(userEmail,authorization);
        //invalid user email - user doesnt exist
        if(userInfo == null){
            logger.severe("Failed to fetch user info for user: "+userEmail);
            return null;
        }
        int startStandId = rideStart.getStartStandId();
        int endStandId = rideStart.getEndStandId();
        int bikeId = rideStart.getBikeId();
        BikeRide bikeRide = startRide(startStandId,endStandId,bikeId,userInfo);
        logger.info("Created user ride for user: "+userEmail+" riding bike: "+bikeId+" from stand: "+startStandId+" to stand: "+endStandId);
        return bikeRide;
    }
    @Override
    public boolean rideExists(String token){
        logger.info("Checking if ride with token: "+token+ " exists.");
        return this.rideManager.getRide(token) != null;
    }

    @Override
    public boolean rideFinished(String rideToken) {
        if(rideToken ==null){
            logger.severe("rideFinished with null token called");
            return false;
        }
        boolean isRideOver = this.rideManager.isRideFinished(rideToken);
        logger.info("Ride with token: "+rideToken+" is over: "+isRideOver);
        if(isRideOver){
            saveRide(rideToken);
        }

        return isRideOver;
    }

    @Override
    public Bike updateLocation(String rideToken, Location bikeLocation) {
        if(rideToken == null || bikeLocation == null){
            logger.severe("updateLocation method invoked with null token or bikeLocation arg");
            return null;
        }
        return this.rideManager.updateBikeLocation(rideToken,bikeLocation);
    }

    @Override
    public Ride saveRide(String rideToken) {
        if(rideToken == null){
            logger.severe("saveRide invoked with null ride token!");
            return null;
        }
        BikeRide ride = this.rideManager.getRide(rideToken);
        if(ride == null){
            logger.severe("Ride with token: "+rideToken+" doesnt exist!");
            return null;
        }
        long bikeId = ride.getBikeId();
        long startStandId = ride.getStartStandId();
        long endStandId = ride.getEndStandId();

        Bike b = this.bikeService.getCurrentBike(bikeId);
        Stand startStand = this.standService.fetchStand(startStandId);
        Stand endStand = this.standService.fetchStand(endStandId);
        OffsetDateTime now = OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC);
        Location standLocation = new Location(endStand.getLongitude(),endStand.getLatitude());
        Ride finishedRide = new Ride(ride.getUserId(),b,startStand,endStand, Date.from(ride.getRideStart().toInstant()), Date.from(now.toInstant()));
        Ride r = this.rideRepository.save(finishedRide);
        this.updateLocation(rideToken,standLocation);

        this.rideManager.endRide(rideToken);
        int rowsAffected = this.bikeService.updateBikeStand(bikeId,endStandId);
        this.bikeService.releaseBike(b);
        logger.info("Saving user ride. Ride finished at: "+now+". Ride started at: "+ride.getRideStart()
        +" bike used: "+bikeId+". Ride saved for user: "+ ride.getUserId());
        return r;
    }

    private User fetchUserInfo(String userEmail,String authorization){
        logger.info("Fetching user info for user: "+userEmail);
        User userInfo = this.userService.fetchUserInfo(userEmail,authorization);
        return userInfo;
    }
    private BikeRide startRide(int startStandId, int endStandId, int bikeId, User userInfo){
        logger.info("Starting ride from stand: "+startStandId+ " to stand: "+endStandId+" on bike: "+bikeId+" for user: "+userInfo.getEmail());
        Stand startStand = this.standService.fetchStand(startStandId);
        if(startStand == null){
            logger.severe("Start stand with id: "+startStandId+" is null");
            return null;
        }
        Stand endStand = this.standService.fetchStand(endStandId);
        if(endStand == null){
            logger.severe("End stand with id: "+endStandId+" is null");

            return null;
        }
        Bike bike = this.bikeService.getCurrentBike((long) bikeId);
        boolean bikeClaimed = this.bikeService.claimBike(bike) != 0;
        if(!bikeClaimed){
            logger.severe("Failed to claim bike with id "+bikeId);

            return null;
        }

        return this.rideManager.startRide(bike,userInfo,startStand,endStand);
    }



}
