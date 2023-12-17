package com.example.bike_sharing.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfiguration {
    @Value("${user.service.code}")
    private String USER_SERVICE_CODE;
    public String getUSER_SERVICE_CODE(){return this.USER_SERVICE_CODE;}

}
