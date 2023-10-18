package com.example.bike_sharing.service;

import com.example.bike_sharing.enums.UserServiceStatus;
import com.example.bike_sharing.model.UserCreate;
import com.example.bike_sharing.model.UserLogin;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Override
    public UserServiceStatus registerUser(UserCreate userRegistrationDto) {
        return null;
    }

    @Override
    public UserServiceStatus loginUser(UserLogin userLoginDto) {
        return null;
    }

    @Override
    public UserServiceStatus logoutUser() {
        return null;
    }
}
