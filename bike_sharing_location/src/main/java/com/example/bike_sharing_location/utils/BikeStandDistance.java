package com.example.bike_sharing_location.utils;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Stand;

public class BikeStandDistance implements LinAlg{
    @Override
    public double computeDistance(Bike bike, Stand stand) {

        final int EARTH_RADIUS = 6371000;

        double bikeLatitude = Math.toRadians(bike.getLatitude());
        double bikeLongitude = Math.toRadians(bike.getLongitude());
        double standLatitude = Math.toRadians(stand.getLatitude());
        double standLongitude = Math.toRadians(stand.getLongitude());

        double longitudeDelta = standLongitude-bikeLongitude;
        double latitudeDelta = standLatitude-bikeLatitude;

        double distance = Math.sin(latitudeDelta / 2) * Math.sin(latitudeDelta / 2) + Math.cos(bikeLatitude) * Math.cos(standLatitude)
                * Math.sin(longitudeDelta / 2) * Math.sin(longitudeDelta / 2);
        distance = EARTH_RADIUS * (2* Math.atan2(Math.sqrt(distance),Math.sqrt(1-distance)));

        return distance;

    }
}
