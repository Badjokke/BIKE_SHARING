package com.example.bike_sharing_location.service.user;

import com.example.bike_sharing_location.configuration.UserServiceConfiguration;
import com.example.bike_sharing_location.model.User;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private final UserServiceBridge userServiceBridge;

    public UserServiceImpl(UserServiceConfiguration userServiceConfiguration){
        this.userServiceBridge = new UserServiceBridge(userServiceConfiguration);
    }
    @Override
    public User fetchUserInfo(String email) {
        if(email == null){
            return null;
        }
        User userInfo = userServiceBridge.fetchUserInfo(email);
        return userInfo;
    }
}
