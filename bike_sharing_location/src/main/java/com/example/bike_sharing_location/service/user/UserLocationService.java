package com.example.bike_sharing_location.service.user;

import com.example.bike_sharing_location.model.User;

/**
 * interface providing methods for communication with user microservice
 */
public interface UserLocationService {
    /**
     * Fetches detailed user info from user microservice
     * @param email email of use whose information is requested
     * @param authorization token - not needed, lazy to delete it from everywhere
     * @return User DTO with information or null if anything failed (user doesnt exist, microservice is down, ...)
     */
    User fetchUserInfo(String email,String authorization);
}
