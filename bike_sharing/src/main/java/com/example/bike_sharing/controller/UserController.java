package com.example.bike_sharing.controller;

import com.example.bike_sharing.authentication.OAuthService;
import com.example.bike_sharing.configuration.AuthConfiguration;
import com.example.bike_sharing.enums.UserServiceStatus;
import com.example.bike_sharing.model.UserCreate;
import com.example.bike_sharing.model.UserCreated;
import com.example.bike_sharing.model.UserLoggedIn;
import com.example.bike_sharing.model.UserLogin;
import com.example.bike_sharing.service.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    private final UserService userService;



    @GetMapping("/logout")
    @Override
    public ResponseEntity<Void> logoutUser(String authorization) {
        return UserApi.super.logoutUser(authorization);
    }

    public UserController(UserService service){
        this.userService = service;
    }


    @PostMapping("/register")
    @Override
    public ResponseEntity<UserCreated> createUser(UserCreate userCreate) {
        final String email = userCreate.getEmail();
        final String userName = userCreate.getUsername();
        final String password = userCreate.getPassword();

        final UserServiceStatus resultStatus = this.userService.registerUser(email,userName,password);
        return sendRegisterResponse(resultStatus);
    }
    @PostMapping("/login")
    @Override
    public ResponseEntity<UserLoggedIn> loginUser(UserLogin userLogin) {

        return UserApi.super.loginUser(userLogin);
    }


    private ResponseEntity<UserCreated> sendRegisterResponse(UserServiceStatus resultStatus){
        UserCreated responseBody = new UserCreated();
        responseBody.message(resultStatus.getLabel());
        return new ResponseEntity<>(responseBody, HttpStatusCode.valueOf(resultStatus.getStatusCode()));
    }





}
