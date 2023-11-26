package com.example.bike_sharing_location.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfiguration {
    @Value("${USER_URL}")
    private String USER_SERVICE_URL;
    @Value("${USER_URL}/user/user_info")
    private String USER_INFO_URL;

    public String getUSER_SERVICE_URL() {
        return USER_SERVICE_URL;
    }

    public String getUSER_INFO_URL() {
        return USER_INFO_URL;
    }
}
