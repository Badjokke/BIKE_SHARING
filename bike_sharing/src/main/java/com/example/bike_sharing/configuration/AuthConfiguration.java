package com.example.bike_sharing.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    @Value("${AUTH_URL}")
    private String AUTH_URL;


    public String getAUTH_URL(){
        return "http://localhost:8082";
    }

}
