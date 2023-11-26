package com.example.bike_sharing_location.service.user;


import com.example.bike_sharing_location.model.User;

public interface UserService {
    User fetchUserInfo(String email);
}
