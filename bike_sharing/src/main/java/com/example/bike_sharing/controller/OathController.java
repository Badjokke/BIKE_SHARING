package com.example.bike_sharing.controller;

import com.example.bike_sharing.model.UserLoggedIn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class OathController implements Oauth2Api{

    @GetMapping("/login/google")
    @Override
    public ResponseEntity<UserLoggedIn> oauth2login() {
        return null;
    }


}
