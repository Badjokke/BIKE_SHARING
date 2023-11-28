package com.example.bike_sharing_location.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BikeServiceConfiguration {
    @Value("${bike.service.period}")
    private double BIKE_SERVICE_INTERVAL;
    @Value("${bike.update_query.period}")
    private long BIKE_SERVICE_UPDATE_INTERVAL;

    public double getBIKE_SERVICE_INTERVAL() {
        return BIKE_SERVICE_INTERVAL;
    }
    public long getBIKE_SERVICE_UPDATE_INTERVAL(){
        return this.BIKE_SERVICE_UPDATE_INTERVAL;
    }


}
