package com.example.bike_sharing.controller;

import com.example.bike_sharing.model.UserCreate;
import com.example.bike_sharing.model.UserCreated;
import com.example.bike_sharing.model.UserLoggedIn;
import com.example.bike_sharing.model.UserLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import com.example.bike_sharing.controller.UserApi;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {

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
    @GetMapping("/logout")
    @Override
    public ResponseEntity<Void> logoutUser() {
        return UserApi.super.logoutUser();
    }
}
