package com.example.bike_sharing_location;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class BikeSharingLocationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeSharingLocationApplication.class, args);
    }

}
