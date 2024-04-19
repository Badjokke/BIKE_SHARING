package com.example.bike_sharing_location.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for ride
 */
@Configuration
public class RideServiceConfiguration {
    //the minimum distance of bike from stand that is considered as "ride completed" distance
    @Value("${bike.min_distance}")
    private double BIKE_MIN_STAND_DISTANCE;
    //hash of this microservice that is used in custom x-service header to mark the request as trustworthy to other services
    @Value("${location.service.code}")
    private String serviceHash;
    public double getBikeMinDistance(){return this.BIKE_MIN_STAND_DISTANCE;}
    public String getServiceHash(){return this.serviceHash;}
}
