package com.example.bike_sharing.controller;

import com.example.bike_sharing.domain.BikeSharingUser;
import com.example.bike_sharing.enums.UserServiceStatus;
import com.example.bike_sharing.model.*;
import com.example.bike_sharing.service.user.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<List<Ride>> userRidesList(String userEmail) {
        return UserApi.super.userRidesList(userEmail);
    }

    @Override
    @PutMapping("/change_role")
    public ResponseEntity<Void> userChangeRolePut(UserChangeRole userChangeRole) {
        BikeSharingUser.Role role = BikeSharingUser.Role.valueOf(userChangeRole.getRole().getValue());

        String email = userChangeRole.getEmail();
        UserServiceStatus operationResult = this.userService.changeUserRole(email,role);

        return new ResponseEntity<>(null,HttpStatusCode.valueOf(operationResult.getStatusCode()));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<User>> userList(Integer serviceMen) {
        List<User> users;
        if(serviceMen == 1){
            users = this.userService.fetchAllServiceman();
        }
        else{
            users = this.userService.fetchAllRegularUsers();
        }

        return new ResponseEntity<>(users,HttpStatusCode.valueOf(200));
    }

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
    public ResponseEntity<UserLoggedIn> createUser(UserCreate userCreate) {
        final String email = userCreate.getEmail();
        final String userName = userCreate.getUsername();
        final String password = userCreate.getPassword();

        String token = this.userService.registerUser(email,userName,password);
        return sendRegisterResponse(token == null?409:200,token);
    }
    @PostMapping("/login")
    @Override
    public ResponseEntity<UserLoggedIn> loginUser(UserLogin userLogin) {
        String email = userLogin.getEmail();
        String password = userLogin.getPassword();
        String token  = this.userService.loginUser(email,password);
        return sendRegisterResponse(token == null?404:200, token);
    }


    private ResponseEntity<UserLoggedIn> sendRegisterResponse(int code, String token){
        UserLoggedIn responseBody = new UserLoggedIn();
        responseBody.setToken(token);
        return new ResponseEntity<>(responseBody, HttpStatusCode.valueOf(code));
    }








}
