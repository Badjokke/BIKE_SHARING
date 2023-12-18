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
import com.example.bike_sharing_location.utils.BikeStandDistance;
import com.example.bike_sharing_location.utils.LinAlg;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class BikeRideService implements RideService{
    private final RideRepository rideRepository;
    private final BikeService bikeService;
    private final StandService standService;
    private final RideManager rideManager;
    private final UserService userService;
    private final RideServiceConfiguration rideConfiguration;
    public BikeRideService(RideRepository rideRepository, UserService userService, BikeService bikeService, StandService standService, RideServiceConfiguration rideConfiguration){
        this.rideRepository = rideRepository;
        this.userService = userService;
        this.bikeService = bikeService;
        this.standService = standService;
        this.rideConfiguration = rideConfiguration;
        this.rideManager = new RideManager(this.bikeService.getInMemoryBikeStorage(),this.standService.getInMemoryStandStorage(),rideConfiguration.getBikeMinDistance());
    }
    @Override
    public List<Ride> fetchUserRides(long userId) {
        return this.rideRepository.fetchUserRides(userId);
    }

    @Override
    public BikeRide createUserRide(RideStart rideStart,String authorization) {
        String userEmail = rideStart.getUserEmail();
        User userInfo = this.fetchUserInfo(userEmail,authorization);
        //invalid user email - user doesnt exist
        if(userInfo == null){
            return null;
        }
        int startStandId = rideStart.getStartStandId();
        int endStandId = rideStart.getEndStandId();
        int bikeId = rideStart.getBikeId();
        BikeRide bikeRide = startRide(startStandId,endStandId,bikeId,userInfo);

        return bikeRide;
    }
    @Override
    public boolean rideExists(String token){
        return this.rideManager.getRide(token) != null;
    }

    @Override
    public boolean rideFinished(String rideToken) {
        if(rideToken ==null){
            return false;
        }
        boolean isRideOver = this.rideManager.isRideFinished(rideToken);
        if(isRideOver)
            saveRide(rideToken);

        return isRideOver;
    }

    @Override
    public Bike updateLocation(String rideToken, Location bikeLocation) {
        if(rideToken == null || bikeLocation == null){
            return null;
        }
        return this.rideManager.updateBikeLocation(rideToken,bikeLocation);
    }

    @Override
    public Ride saveRide(String rideToken) {
        if(rideToken == null){
            return null;
        }
        BikeRide ride = this.rideManager.getRide(rideToken);
        if(ride == null){
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
        return r;
    }

    private User fetchUserInfo(String userEmail,String authorization){
        User userInfo = this.userService.fetchUserInfo(userEmail,authorization);
        return userInfo;
    }
    private BikeRide startRide(int startStandId, int endStandId, int bikeId, User userInfo){
        Stand startStand = this.standService.fetchStand(startStandId);
        if(startStand == null){
            return null;
        }
        Stand endStand = this.standService.fetchStand(endStandId);
        if(endStand == null){
            return null;
        }
        Bike bike = this.bikeService.getCurrentBike((long) bikeId);
        boolean bikeClaimed = this.bikeService.claimBike(bike) != 0;
        if(!bikeClaimed){
            return null;
        }
        return this.rideManager.startRide(bike,userInfo,startStand,endStand);
    }



}
