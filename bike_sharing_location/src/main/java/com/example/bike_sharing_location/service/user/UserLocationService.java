package com.example.bike_sharing_location.service.user;

import com.example.bike_sharing_location.model.User;

public interface UserLocationService {
    User fetchUserInfo(String email,String authorization);
}
