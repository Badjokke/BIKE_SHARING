package com.example.bike_sharing_location.controller;

import com.example.bike_sharing_location.model.RideStart;
import com.example.bike_sharing_location.model.RideStart201Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RideController implements RideApi {
    @Override
    public ResponseEntity<RideStart201Response> rideStart(RideStart rideStart) {
        return RideApi.super.rideStart(rideStart);
    }
}
