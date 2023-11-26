package com.example.bike_sharing_location.controller;

import com.example.bike_sharing_location.domain.Ride;
import com.example.bike_sharing_location.mapper.DomainToDto;
import com.example.bike_sharing_location.mapper.RideToUserRide;
import com.example.bike_sharing_location.model.BikeRide;
import com.example.bike_sharing_location.model.RideStart;
import com.example.bike_sharing_location.model.RideStart201Response;
import com.example.bike_sharing_location.model.UserRide;
import com.example.bike_sharing_location.service.ride.RideService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ride")
public class RideController implements RideApi {
    private final RideService rideService;
    public RideController(RideService rideService){
        this.rideService = rideService;
    }
    @Override
    @PostMapping("/start")
    public ResponseEntity<RideStart201Response> rideStart(RideStart rideStart) {
        BikeRide rideStarted = this.rideService.createUserRide(rideStart);
        if(rideStarted == null){
            return ResponseEntity.badRequest().build();
        }
        RideStart201Response response = new RideStart201Response();
        response.rideId(rideStarted.getBikeId().intValue()).message("Bike ride created");
        return new ResponseEntity<>(response,HttpStatusCode.valueOf(201));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserRide>> rideList(Long userId) {
        if(userId == null){
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
        List<Ride> rides = this.rideService.fetchUserRides(userId);
        DomainToDto<Ride,UserRide> mapper = new RideToUserRide();
        List<UserRide> userRides = mapper.mapDomainToDtos(rides);
        return ResponseEntity.ok(userRides);

    }
}
