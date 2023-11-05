package com.example.bike_sharing_location.controller;

import com.example.bike_sharing_location.model.BikeLocationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BikeController {

     @MessageMapping("/bike_location")
     @SendTo("/topic/messages")
    public String dummyLocation(String greeting){
         return "[" + 32+ ": " + greeting+"]";
     }



}
