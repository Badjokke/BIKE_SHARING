package com.example.bike_sharing_location.service.user;

import com.example.bike_sharing_location.configuration.UserServiceConfiguration;
import com.example.bike_sharing_location.model.User;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
public class UserServiceImpl implements UserService {
    private final UserServiceBridge userServiceBridge;
    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    public UserServiceImpl(UserServiceConfiguration userServiceConfiguration){
        this.userServiceBridge = new UserServiceBridge(userServiceConfiguration);
    }
    @Override
    public User fetchUserInfo(String email,String authorization) {
        logger.info("Fetching user info for user: "+email);
        if(email == null){
            return null;
        }
        User userInfo = userServiceBridge.fetchUserInfo(email,authorization);
        logger.info("User info for user: "+email+","+userInfo);
        return userInfo;
    }
}
