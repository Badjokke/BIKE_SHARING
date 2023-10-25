package com.example.bike_sharing.controller;

import com.example.bike_sharing.authentication.OAuthService;
import com.example.bike_sharing.configuration.AuthConfiguration;
import com.example.bike_sharing.model.UserCreate;
import com.example.bike_sharing.model.UserCreated;
import com.example.bike_sharing.model.UserLoggedIn;
import com.example.bike_sharing.model.UserLogin;
import com.example.bike_sharing.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    private final UserService userService;
    private final OAuthService oAuthService;
    @GetMapping("/logout")
    @Override
    public ResponseEntity<Void> logoutUser(String authorization) {
        AuthConfiguration configuration = new AuthConfiguration();
        String hello = configuration.getAUTH_URL();
        return UserApi.super.logoutUser(authorization);
    }

    public UserController(UserService service, OAuthService oAuthService){
        this.userService = service;
        this.oAuthService = oAuthService;
    }


    @PostMapping("/register")
    @Override
    public ResponseEntity<UserCreated> createUser(UserCreate userCreate) {
        return UserApi.super.createUser(userCreate);
    }
    @PostMapping("/login")
    @Override
    public ResponseEntity<UserLoggedIn> loginUser(UserLogin userLogin) {

        return UserApi.super.loginUser(userLogin);
    }

}
