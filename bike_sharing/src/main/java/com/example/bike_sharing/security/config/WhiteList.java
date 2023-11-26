package com.example.bike_sharing.security.config;

public class WhiteList {

        public static final String[] NO_AUTHORIZATION_NEEDED = new String[]{
                "/user/register",
                "/user/login",
                "/user/change_role",
                "/login",
                "/favicon.ico",
                "/user/list",
                "/user/user_info",
                "/user/rides"
        };
}
