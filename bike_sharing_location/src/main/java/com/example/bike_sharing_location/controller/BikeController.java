package com.example.bike_sharing_location.controller;
import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.mapper.BikeToBikeDto;
import com.example.bike_sharing_location.mapper.BikeToBikeLocationDto;
import com.example.bike_sharing_location.mapper.DomainToDto;
import com.example.bike_sharing_location.model.*;
import com.example.bike_sharing_location.service.bike.BikeService;
import com.google.gson.Gson;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of controller
 * for endpoint description refer to api.yaml in resources
 */
@RestController
@RequestMapping("/bike")
public class BikeController implements BikeApi {
    //business logic for bikes implemented in service layer
    private final BikeService bikeService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger logger = Logger.getLogger(BikeController.class.getName());
    public BikeController(BikeService bikeService, SimpMessagingTemplate simpMessagingTemplate){
        this.bikeService = bikeService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        logger.info("BikeController bean created");
    }


    /**
     *  Endpoint that serves bike service request - serviceman tagging bike as "serviced"
     * @param authorization Access token for authentication (required)
     * @param bikeState Wrapper for deserialization of json request body (required)
     * @return ResponseEntity:
     *                      - 200 if request is valid - ie the bike exists and user has SERVICEMAN role
     *                      - 400 otherwise - ie bike doesn´t exist, user doesn´t exist or has REGULAR role - should be 401
     */
    @Override
    @PutMapping("/service")
    public ResponseEntity<ChangeBikeState200Response> changeBikeState(String authorization,BikeState bikeState) {
        if(bikeState.getBikeId() == null || bikeState.getUserEmail() == null){
            logger.warning("Invalid request for /service. bikeId or userEmail is null.");
            return ResponseEntity.badRequest().body(null);
        }
        long bikeId = bikeState.getBikeId();
        boolean serviced = this.bikeService.markBikeAsServiced(bikeId, bikeState.getUserEmail(),authorization);
        ChangeBikeState200Response response = new ChangeBikeState200Response();

        if(!serviced){
            logger.warning("Failed to mark bike "+bikeId+" as serviced.");
            return ResponseEntity.badRequest().body(null);
        }
        logger.info("Bike with id: "+bikeId+ " serviced.");
        response.message("Bike with id: "+bikeId+" marked as serviced");
        return ResponseEntity.ok(response);
    }

    /**
     * Returns list of all bikes
     * @param token Access token for authentication (required)
     * @return 200 with json array of bikes
     */
    @Override
    @GetMapping("/list")
    public ResponseEntity<List<BikeDto>> fetchBikes(@RequestHeader(name="Authorization") String token) {
        logger.info("Request for bike data");
        List<Bike> rideableBikes = this.bikeService.getRideableBikes();
        DomainToDto<Bike,BikeDto> mapper = new BikeToBikeDto();
        List<BikeDto> bikeDtos = mapper.mapDomainToDtos(rideableBikes);
        logger.info("Returning array with "+ bikeDtos.size()+ " bikes");
        return ResponseEntity.ok(bikeDtos);

    }

    /**
     * Fetches all bikes that are due for service
     * @return 200 with json array of all bikes due for service
     */
    @Override
    @GetMapping("/service")
    public ResponseEntity<List<BikeDto>> fetchServiceableBikes() {
        logger.info("Request for bikes due for service");
        List<Bike> bikesForService = this.bikeService.getBikesDueForService();
        DomainToDto<Bike,BikeDto> mapper = new BikeToBikeDto();
        List<BikeDto> bikeDtos = mapper.mapDomainToDtos(bikesForService);
        logger.info("Returning list of bikes due for service, list len: "+bikeDtos.size());
        return ResponseEntity.ok(bikeDtos);
    }

    /**
     *
     * @param bikeLocationMessage wrapper around STOMP message for bike location update
     *                            contains id of bike whose location is wanted - if 0 is provided, all bikes are streamed
     *                            otherwise only the bike with that id is being streamed
     */
    @MessageMapping("/bike")
    public void fetchBikeLocations(StompBikeLocationMessageWrapper bikeLocationMessage) {
        long bikeId = bikeLocationMessage.getBikeId();
        logger.info("Message requesting bike location received, bike id (0 for all bikes): "+bikeId);

        DomainToDto<Bike,ObjectLocation> mapper = new BikeToBikeLocationDto();
        String json;
        List<Bike>bikes = bikeId == 0?this.bikeService.getAllCurrentBikes(): Arrays.asList(this.bikeService.getCurrentBike(bikeId));
        List<ObjectLocation>  locations = mapper.mapDomainToDtos(bikes);
        json = new Gson().toJson(locations);
        String communicationChannel = "/bike_location/location/"+bikeId;
        logger.info("Sending location of: "+locations.size()+" bikes to channel: "+communicationChannel+ ". Bike data: "+json);
        this.simpMessagingTemplate.convertAndSend(communicationChannel,json);
    }



}
