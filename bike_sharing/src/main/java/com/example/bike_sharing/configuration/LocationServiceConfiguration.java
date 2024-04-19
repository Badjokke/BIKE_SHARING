package com.example.bike_sharing.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocationServiceConfiguration {
    @Value("${LOCATION_URL}/ride/list")
    private String USER_RIDES_URL;
    @Value("${location.service.code}")
    private String LOCATION_SERVICE_CODE;
    public String getUSER_RIDES_URL(){
        return this.USER_RIDES_URL;
    }
    public String getLOCATION_SERVICE_CODE(){return this.LOCATION_SERVICE_CODE;}
}
