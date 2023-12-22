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
    public ResponseEntity<ChangeBikeState200Response> changeBikeState(String authorization,BikeState bikeState) {
        if(bikeState.getBikeId() == null || bikeState.getUserEmail() == null){
            return ResponseEntity.badRequest().body(null);
        }
        long bikeId = bikeState.getBikeId();
        boolean serviced = this.bikeService.markBikeAsServiced(bikeId, bikeState.getUserEmail(),authorization);
        ChangeBikeState200Response response = new ChangeBikeState200Response();

        if(!serviced){
            return ResponseEntity.badRequest().body(null);
        }
        response.message("Bike with id: "+bikeId+" marked as serviced");
        return ResponseEntity.ok(response);
    }

    @Override
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
        List<Bike>bikes = bikeId == 0?this.bikeService.getAllCurrentBikes(): Arrays.asList(this.bikeService.getCurrentBike(bikeId));
        List<ObjectLocation>  locations = mapper.mapDomainToDtos(bikes);
        json = new Gson().toJson(locations);

        String communicationChannel = "/bike_location/location/"+bikeId;

        this.simpMessagingTemplate.convertAndSend(communicationChannel,json);
    }



}
