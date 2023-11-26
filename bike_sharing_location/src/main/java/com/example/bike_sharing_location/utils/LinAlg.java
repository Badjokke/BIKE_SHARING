package com.example.bike_sharing_location.utils;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Stand;

public interface LinAlg {
    double computeDistance(Bike bike, Stand stand);
}
