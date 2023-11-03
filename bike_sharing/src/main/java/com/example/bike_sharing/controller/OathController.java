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
    @RequestMapping("/login")
    public String renderLogin(){
        return "<div>\n" +
                "    <h2>Please Login</h2>\n" +
                "    <br/>\n" +
                "</div>\n" +
                "<div>\n" +
                "    <h4><a th:href=\"/@{/oauth2/login/google}\">Login with Google</a></h4>   \n" +
                "</div>\n";
    }

}
