package com.example.bike_sharing.service.location;

import com.example.bike_sharing.model.Ride;

import java.util.List;

public interface LocationService {

    List<Ride> fetchUserRides(long userId,String authorization);


}
