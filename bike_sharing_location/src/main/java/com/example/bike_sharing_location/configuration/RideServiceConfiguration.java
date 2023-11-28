package com.example.bike_sharing_location.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RideServiceConfiguration {
    @Value("${bike.min_distance}")
    private double BIKE_MIN_STAND_DISTANCE;
    public double getBikeMinDistance(){return this.BIKE_MIN_STAND_DISTANCE;}
}
