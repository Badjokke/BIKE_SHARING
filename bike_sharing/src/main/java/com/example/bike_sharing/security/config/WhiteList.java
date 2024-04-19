package com.example.bike_sharing.security.config;

public class WhiteList {

        public static final String[] NO_AUTHORIZATION_NEEDED = new String[]{
                "/user/register",
                "/user/login",
                "/login",
        };
}
