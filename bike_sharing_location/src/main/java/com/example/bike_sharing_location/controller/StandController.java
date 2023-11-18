package com.example.bike_sharing_location.controller;

import com.example.bike_sharing_location.model.ObjectLocation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StandController implements StandApi{
    @Override
    public ResponseEntity<List<ObjectLocation>> fetchStands(Integer standId) {
        return StandApi.super.fetchStands(standId);
    }
}
