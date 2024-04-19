package com.example.bike_sharing.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthServiceConfiguration {
    @Value("${AUTH_URL}")
    private String AUTH_BASE_URL;
    @Value("${AUTH_URL}/authenticate")
    private String VERIFY_TOKEN_ENDPOINT;
    @Value("${AUTH_URL}/logout")
    private String TOKEN_INVALIDATION_ENDPOINT;
    @Value("${AUTH_URL}/login")
    private String GENERATE_TOKEN_ENDPOINT;


    public String getAUTH_BASE_URL() {
        return AUTH_BASE_URL;
    }

    public String getVERIFY_TOKEN_ENDPOINT() {
        return VERIFY_TOKEN_ENDPOINT;
    }

    public String getTOKEN_INVALIDATION_ENDPOINT() {
        return TOKEN_INVALIDATION_ENDPOINT;
    }

    public String getGENERATE_TOKEN_ENDPOINT() {
        return GENERATE_TOKEN_ENDPOINT;
    }
}
