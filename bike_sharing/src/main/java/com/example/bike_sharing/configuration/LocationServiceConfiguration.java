package com.example.bike_sharing.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocationServiceConfiguration {
    @Value("${LOCATION_URL}")
    private String LOCATION_BASE_URL;
    @Value("${LOCATION_URL}/bike/list")
    private String BIKE_LIST_URL;
    @Value("${LOCATION_URL}/ride/list")
    private String USER_RIDES_URL;
    public String getLOCATION_BASE_URL() {
        return LOCATION_BASE_URL;
    }

    public String getBIKE_LIST_URL() {
        return BIKE_LIST_URL;
    }
    public String getUSER_RIDES_URL(){
        return this.USER_RIDES_URL;
    }
}
