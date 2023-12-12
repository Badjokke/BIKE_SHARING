package com.example.bike_sharing.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientAppConfiguration {
    @Value("${CLIENT_URL}")
    private String clientUrl;
    @Value("${CLIENT_URL}/oauth_login")
    private String redirectUrl;


    public String getClientUrl(){
        return this.clientUrl;
    }
    public String getRedirectUrl(){
        return this.redirectUrl;
    }

}
