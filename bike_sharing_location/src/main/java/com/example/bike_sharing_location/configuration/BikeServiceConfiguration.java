package com.example.bike_sharing_location.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BikeServiceConfiguration {
    @Value("${bike.service.period}")
    private double BIKE_SERVICE_INTERVAL;


    public double getBIKE_SERVICE_INTERVAL() {
        return BIKE_SERVICE_INTERVAL;
    }
}
