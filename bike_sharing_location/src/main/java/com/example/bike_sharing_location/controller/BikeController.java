package com.example.bike_sharing_location.controller;

import com.example.bike_sharing_location.model.*;
import com.example.bike_sharing_location.service.bike.BikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bike")

public class BikeController implements BikeApi {
    private final BikeService bikeService;

    public BikeController(BikeService bikeService){
        this.bikeService = bikeService;
    }
    @PutMapping("/change_state")
    @Override
    public ResponseEntity<ChangeBikeState200Response> changeBikeState(BikeState bikeState) {
        return BikeApi.super.changeBikeState(bikeState);
    }
    @GetMapping("")
    @Override
    public ResponseEntity<List<ObjectLocation>> fetchBikeLocations(Integer bikeId) {
        return BikeApi.super.fetchBikeLocations(bikeId);
    }

    @Override
    public ResponseEntity<List<Bike>> fetchBikes() {
        return BikeApi.super.fetchBikes();
    }

    @MessageMapping("/bike")
    @SendTo("/bike_location/location")
    public String greeting(StompMessageWrapper message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return "message";
    }
}
