package com.example.bike_sharing_location.service.ride;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Ride;
import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.model.BikeRide;
import com.example.bike_sharing_location.model.RideStart;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.RideRepository;
import com.example.bike_sharing_location.ride.RideManager;
import com.example.bike_sharing_location.service.bike.BikeService;
import com.example.bike_sharing_location.service.stand.StandService;
import com.example.bike_sharing_location.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeRideService implements RideService{
    private final RideRepository rideRepository;
    private final BikeService bikeService;
    private final StandService standService;
    private final RideManager rideManager;
    private final UserService userService;
    public BikeRideService(RideRepository rideRepository, UserService userService, BikeService bikeService, StandService standService){
        this.rideRepository = rideRepository;
        this.userService = userService;
        this.bikeService = bikeService;
        this.standService = standService;
        this.rideManager = new RideManager(this.bikeService.getInMemoryBikeStorage(),this.standService.getInMemoryStandStorage());
    }
    @Override
    public List<Ride> fetchUserRides(long userId) {
        return this.rideRepository.fetchUserRides(userId);
    }

    @Override
    public BikeRide createUserRide(RideStart rideStart) {
        String userEmail = rideStart.getUserEmail();
        User userInfo = this.fetchUserInfo(userEmail);
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

    private User fetchUserInfo(String userEmail){
        User userInfo = this.userService.fetchUserInfo(userEmail);
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
        if(this.bikeService.isBikeRideable(bike)){
            return null;
        }
        return this.rideManager.startRide(bike,userInfo,startStand,endStand);
    }



}
