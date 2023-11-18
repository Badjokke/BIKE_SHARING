package com.example.bike_sharing_location.controller;

import com.example.bike_sharing_location.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BikeController implements BikeApi {

    @Override
    public ResponseEntity<ChangeBikeState200Response> changeBikeState(BikeState bikeState) {
        return BikeApi.super.changeBikeState(bikeState);
    }

    @Override
    public ResponseEntity<List<ObjectLocation>> fetchBikeLocations(Integer bikeId) {
        return BikeApi.super.fetchBikeLocations(bikeId);
    }

    @Override
    public ResponseEntity<List<Bike>> fetchBikes() {
        return BikeApi.super.fetchBikes();
    }

    @MessageMapping("/stand")
    @SendTo("/topic/greetings")
    public String greeting(StompMessageWrapper message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return "message";
    }
}
