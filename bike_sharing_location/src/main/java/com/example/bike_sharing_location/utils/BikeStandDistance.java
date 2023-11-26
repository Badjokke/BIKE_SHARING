package com.example.bike_sharing_location.utils;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Stand;

public class BikeStandDistance implements LinAlg{
    @Override
    public double computeDistance(Bike bike, Stand stand) {
        double deltaLongitude = bike.getLongitude()-stand.getLongitude();
        double deltaLatitude = bike.getLatitude()-stand.getLatitude();
        return Math.sqrt((deltaLongitude*deltaLongitude) + (deltaLatitude*deltaLatitude));

    }
}
