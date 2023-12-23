package com.example.bike_sharing_location.security.config;

public class WhiteList {
        /**
         * public endpoints - JWT filter will not try to authenticate user requesting data from these endpoints
         */
        public static final String[] NO_AUTHORIZATION_NEEDED = new String[]{
                "/bike",
                "/ride",
                "/stand/location",

        };
}
