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

import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of controller
 * for endpoint description, refer to api.yaml in resources
 */
@RestController
@RequestMapping("/ride")
public class RideController implements RideApi {
    private final RideService rideService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger logger = Logger.getLogger(RideController.class.getName());
    public RideController(RideService rideService, SimpMessagingTemplate simpMessagingTemplate){
        this.rideService = rideService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    /**
     * Endpoint to start a new bike ride.
     *
     * @param authorization Access token for authentication (required)
     * @param rideStart      Wrapper for deserialization of json request body (required)
     * @return ResponseEntity:
     * - 201 if the request is valid - i.e., userEmail is valid, bike is free (not for service or on a ride), standId is valid so on
     * - 400 otherwise - i.e., invalid request or ride creation failed
     */
    @Override
    @PostMapping("/start")
    public ResponseEntity<RideStart201Response> rideStart(String authorization,RideStart rideStart) {
        logger.info("Request for ride creation by user. Request json "+ new Gson().toJson(rideStart));
        BikeRide rideStarted = this.rideService.createUserRide(rideStart, authorization);
        if(rideStarted == null){
            logger.warning("Failed to start requested ride");
            return ResponseEntity.badRequest().build();
        }
        RideStart201Response response = new RideStart201Response();
        logger.info("Ride started and saved in memory. User id: "+rideStarted.getUserId()+", user email: "+
                rideStart.getUserEmail()+
                ", bike id: "+
                rideStarted.getBikeId()+ ", from stand: "+rideStarted.getStartStandId()+" to stand: "+rideStarted.getEndStandId());
        response.rideId(rideStarted.getBikeId().intValue()).message("Bike ride created").token(rideStarted.getRideToken());
        return new ResponseEntity<>(response,HttpStatusCode.valueOf(201));
    }


    /**
     * Endpoint to fetch the list of rides for a user.
     *
     * @param userId User ID for which rides are requested (required)
     * @return ResponseEntity:
     * - 200 with json array of user rides if the request is valid
     * - 400 otherwise - i.e., invalid request or missing user ID
     */
    @Override
    @GetMapping("/list")
    public ResponseEntity<List<UserRide>> rideList(Integer userId) {
        logger.info("Request for list of rides: "+userId);
        if(userId == null){
            logger.warning("user id is not provided.");
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
        List<Ride> rides = this.rideService.fetchUserRides(Long.valueOf(userId));
        DomainToDto<Ride,UserRide> mapper = new RideToUserRide();
        List<UserRide> userRides = mapper.mapDomainToDtos(rides);
        logger.info("Return user rides list, size: "+userRides.size()+" response: "+userRides);
        return ResponseEntity.ok(userRides);

    }

    /**
     * Message endpoint for bike ride - updates location of bikes in memory storage and sends back the location of bike
     * @param bikeLocationMessage wrapper around STOMP message of bike location from client
     *                            contains the location of bike
     */
    @MessageMapping("/ride")
    public void userBikeRide(StompBikeRideMessageWrapper bikeLocationMessage){
        String bikeRideToken = bikeLocationMessage.getRideToken();
        Location bikeLocation = bikeLocationMessage.getLocation();
        logger.info("Received message with bike location update: "+bikeLocationMessage+ " for ride: "+bikeRideToken);

        DomainToDto<Bike,ObjectLocation> mapper = new BikeToBikeLocationDto();

        Bike bike = this.rideService.updateLocation(bikeRideToken,bikeLocation);
        //something went wrong
        if(bike == null){
            logger.severe("Bike ride: "+bikeRideToken+" has invalid bike. Data inconsistency.");
            return;
        }
        ObjectLocation location = mapper.mapDomainToDto(bike);
        String message = new Gson().toJson(location);
        logger.info("Sending bike location of bike "+bike.getId()+" on ride to channel /bike_ride/"+bikeRideToken+
                ". Location: "+location);
        this.simpMessagingTemplate.convertAndSend("/bike_ride/"+bikeRideToken,message);

        if(this.rideService.rideFinished(bikeRideToken)){
            logger.info("Ride: "+bikeRideToken+" finished.");

            message = "{" + "message: "+bikeRideToken+" successfully finished"+"}";
            this.simpMessagingTemplate.convertAndSend("/bike_ride/close/"+bikeRideToken,message);
        }
    }
}
