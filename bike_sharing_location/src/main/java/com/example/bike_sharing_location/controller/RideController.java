package com.example.bike_sharing_location.controller;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Ride;
import com.example.bike_sharing_location.mapper.BikeToBikeLocationDto;
import com.example.bike_sharing_location.mapper.DomainToDto;
import com.example.bike_sharing_location.mapper.RideToUserRide;
import com.example.bike_sharing_location.model.*;
import com.example.bike_sharing_location.service.ride.RideService;
import com.google.gson.Gson;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/ride")
public class RideController implements RideApi {
    private final RideService rideService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public RideController(RideService rideService, SimpMessagingTemplate simpMessagingTemplate){
        this.rideService = rideService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }



    @Override
    @PostMapping("/start")
    public ResponseEntity<RideStart201Response> rideStart(String authorization,RideStart rideStart) {
        BikeRide rideStarted = this.rideService.createUserRide(rideStart, authorization);
        if(rideStarted == null){
            return ResponseEntity.badRequest().build();
        }
        RideStart201Response response = new RideStart201Response();
        response.rideId(rideStarted.getBikeId().intValue()).message("Bike ride created").token(rideStarted.getRideToken());
        return new ResponseEntity<>(response,HttpStatusCode.valueOf(201));
    }



    @Override
    @GetMapping("/list")
    public ResponseEntity<List<UserRide>> rideList(Integer userId) {
        if(userId == null){
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
        List<Ride> rides = this.rideService.fetchUserRides(Long.valueOf(userId));
        DomainToDto<Ride,UserRide> mapper = new RideToUserRide();
        List<UserRide> userRides = mapper.mapDomainToDtos(rides);
        return ResponseEntity.ok(userRides);

    }
    @MessageMapping("/ride")
    public void userBikeRide(StompBikeRideMessageWrapper bikeLocationMessage){
        String bikeRideToken = bikeLocationMessage.getRideToken();
        Location bikeLocation = bikeLocationMessage.getLocation();
        DomainToDto<Bike,ObjectLocation> mapper = new BikeToBikeLocationDto();

        Bike bike = this.rideService.updateLocation(bikeRideToken,bikeLocation);
        //something went wrong
        if(bike == null){
            return;
        }
        String message;
        if(this.rideService.rideFinished(bikeRideToken)){
            message = "{" + "message: "+bikeRideToken+" successfully finished"+"}";
            this.simpMessagingTemplate.convertAndSend("/bike_ride/close/"+bikeRideToken,message);
            return;
        }
        ObjectLocation location = mapper.mapDomainToDto(bike);
        message = new Gson().toJson(location);
        this.simpMessagingTemplate.convertAndSend("/bike_ride/"+bikeRideToken,message);
    }
}
