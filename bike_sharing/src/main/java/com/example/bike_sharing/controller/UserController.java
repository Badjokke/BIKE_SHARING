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
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    private final UserService userService;
    @Override
    @GetMapping("/user_info")
    public ResponseEntity<User> userInfo(String userEmail) {
        BikeSharingUser u = this.userService.fetchUserInfo(userEmail);
        if(u == null || u.getId() == 0){
            return ResponseEntity.badRequest().build();
        }
        User user = new User();
        user.username(u.getName()).id(u.getId()).email(u.getEmailAddress()).role(User.RoleEnum.valueOf(u.getRole().name()));
        return ResponseEntity.ok(user);
    }



    @Override
    @GetMapping("/rides")
    public ResponseEntity<List<Ride>> userRidesList(String userEmail,String authorization) {
        List<Ride> userRides = userService.fetchUserRides(userEmail,authorization);
        if(userRides == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userRides);
    }

    @Override
    @PutMapping("/change_role")
    public ResponseEntity<Void> changeRole(UserChangeRole userChangeRole) {
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
        final UserCreate.UsertypeEnum type = userCreate.getUsertype();
        if(type == null){
            return sendRegisterResponse(400,null, null);
        }
        String token = this.userService.registerUser(email,userName,password,BikeSharingUser.Role.valueOf(type.name()));
        return sendRegisterResponse(token == null?409:200,token, type.name());
    }
    @PostMapping("/login")
    @Override
    public ResponseEntity<UserLoggedIn> loginUser(UserLogin userLogin) {
        String email = userLogin.getEmail();
        String password = userLogin.getPassword();
        Map<String,String> userLoginInfo  = this.userService.loginUser(email,password);
        String token = null;
        String role = null;
        int code = 404;
        if(userLoginInfo != null){
            token = userLoginInfo.get("token");
            role = userLoginInfo.get("role");
            code = 200;
        }
        return sendRegisterResponse(code,token,role);
    }


    private ResponseEntity<UserLoggedIn> sendRegisterResponse(int code, String token, String role){
        UserLoggedIn responseBody = new UserLoggedIn();
        responseBody.setToken(token);
        responseBody.setRole(role);
        return new ResponseEntity<>(responseBody, HttpStatusCode.valueOf(code));
    }








}
