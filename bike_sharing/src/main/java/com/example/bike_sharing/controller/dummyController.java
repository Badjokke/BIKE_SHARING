package com.example.bike_sharing.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class dummyController {

    @GetMapping("/")
    public String helloWorld(){
        return "hello world";
    }



}
