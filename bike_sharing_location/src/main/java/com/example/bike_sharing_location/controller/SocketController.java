package com.example.bike_sharing_location.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    static class HelloMessage{
        private String content;
        public HelloMessage(String content){
            this.content = content;
        }
        public HelloMessage(){

        }

        public void setContent(String content){
            this.content = content;
        }
    }


    @MessageMapping("/stand")
    @SendTo("/topic/greetings")
    public String greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return "message";
    }
}
