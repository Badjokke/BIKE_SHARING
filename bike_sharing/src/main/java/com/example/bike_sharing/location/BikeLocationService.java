package com.example.bike_sharing.location;

import com.example.bike_sharing.model.Ride;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BikeLocationService {

    List<Ride> fetchUserRides(long userId);
}
