package com.example.bike_sharing_location.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for client application - used for redirect on successful login via oauth2
 */
@Configuration
public class ClientAppConfiguration {
    @Value("${CLIENT_URL}")
    private String clientUrl;


    public String getClientUrl(){
        return this.clientUrl;
    }


}