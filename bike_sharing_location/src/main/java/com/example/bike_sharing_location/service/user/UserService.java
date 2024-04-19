package com.example.bike_sharing_location.service.user;


import com.example.bike_sharing_location.model.User;

/**
 * Service used for fetching user information
 */
public interface UserService {
    User fetchUserInfo(String email,String authorization);
}
