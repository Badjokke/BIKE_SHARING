package com.example.bike_sharing.service.location;

import com.example.bike_sharing.configuration.LocationServiceConfiguration;
import com.example.bike_sharing.location.BikeLocationService;
import com.example.bike_sharing.location.LocationServiceBridge;
import com.example.bike_sharing.model.Ride;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeStandLocationService implements  LocationService{
    private final BikeLocationService bikeLocationService;
    public BikeStandLocationService(LocationServiceConfiguration locationServiceConfiguration){
        this.bikeLocationService = new LocationServiceBridge(locationServiceConfiguration.getUSER_RIDES_URL());
    }
    @Override
    public List<Ride> fetchUserRides(long userId, String authorization) {
        List<Ride> responseRides = this.bikeLocationService.fetchUserRides(userId,authorization);
        return responseRides;
    }
}
