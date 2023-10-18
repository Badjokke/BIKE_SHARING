package com.example.bike_sharing.service;

import com.example.bike_sharing.enums.UserServiceStatus;
import com.example.bike_sharing.model.UserCreate;
import com.example.bike_sharing.model.UserLogin;

public interface UserService {


    UserServiceStatus registerUser(UserCreate userRegistrationDto);
    UserServiceStatus loginUser(UserLogin userLoginDto);
    UserServiceStatus logoutUser();


}
