package com.example.bike_sharing_location.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for bike service
 */
@Configuration
public class BikeServiceConfiguration {
    //how many months since last bike service before bike is marked as "up for service"
    @Value("${bike.service.period}")
    private double BIKE_SERVICE_INTERVAL;
    //period (in seconds) of updating bike locations from in memory storage to database
    @Value("${bike.update_query.period}")
    private long BIKE_SERVICE_UPDATE_INTERVAL;

    public double getBIKE_SERVICE_INTERVAL() {
        return BIKE_SERVICE_INTERVAL;
    }
    public long getBIKE_SERVICE_UPDATE_INTERVAL(){
        return this.BIKE_SERVICE_UPDATE_INTERVAL;
    }


}
