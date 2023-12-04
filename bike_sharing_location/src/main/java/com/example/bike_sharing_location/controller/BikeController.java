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
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/bike")
public class BikeController implements BikeApi {
    private final BikeService bikeService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public BikeController(BikeService bikeService, SimpMessagingTemplate simpMessagingTemplate){
        this.bikeService = bikeService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    @PutMapping("/service")
    public ResponseEntity<ChangeBikeState200Response> changeBikeState(BikeState bikeState) {
        long bikeId = bikeState.getBikeId();
        OffsetDateTime bikeStandStamp = bikeState.getServicedStamp();
        this.bikeService.markBikeAsServiced(bikeId);
        ChangeBikeState200Response response = new ChangeBikeState200Response();
        response.message("Bike with id: "+bikeId+" marked as serviced");
        return ResponseEntity.ok(response);
    }

    //@Override
    @GetMapping("/list")
    public ResponseEntity<List<BikeDto>> fetchBikes(@RequestHeader(name="Authorization") String token) {
        List<Bike> rideableBikes = this.bikeService.getRideableBikes();
        DomainToDto<Bike,BikeDto> mapper = new BikeToBikeDto();
        List<BikeDto> bikeDtos = mapper.mapDomainToDtos(rideableBikes);
        return ResponseEntity.ok(bikeDtos);

    }

    @Override
    @GetMapping("/service")
    public ResponseEntity<List<BikeDto>> fetchServiceableBikes() {
        List<Bike> bikesForService = this.bikeService.getBikesDueForService();
        DomainToDto<Bike,BikeDto> mapper = new BikeToBikeDto();
        List<BikeDto> bikeDtos = mapper.mapDomainToDtos(bikesForService);
        return ResponseEntity.ok(bikeDtos);
    }

    @MessageMapping("/bike")
    public void fetchBikeLocations(StompBikeLocationMessageWrapper bikeLocationMessage) {
        long bikeId = bikeLocationMessage.getBikeId();
        DomainToDto<Bike,ObjectLocation> mapper = new BikeToBikeLocationDto();
        String json;
        if(bikeId == 0){
            List<Bike> bikes = this.bikeService.getAllCurrentBikes();
            List<ObjectLocation>  locations = mapper.mapDomainToDtos(bikes);
            json = new Gson().toJson(locations);
        }
        else {
            Bike subscribedbike = this.bikeService.getCurrentBike(bikeId);
            ObjectLocation location = mapper.mapDomainToDto(subscribedbike);
            json = new Gson().toJson(location);
        }
        String communicationChannel = "/bike_location/location/"+bikeId;

        this.simpMessagingTemplate.convertAndSend(communicationChannel,json);
    }



}
