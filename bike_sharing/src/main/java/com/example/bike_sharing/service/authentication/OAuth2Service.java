package com.example.bike_sharing.service.authentication;

import com.example.bike_sharing.domain.BikeSharingUser;
import com.example.bike_sharing.model.OathUser;
import com.example.bike_sharing.service.user.UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Service extends DefaultOAuth2UserService {
    private final UserService userService;
    public OAuth2Service(UserService userService){
        this.userService = userService;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        String token = userRequest.getAccessToken().getTokenValue();
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        this.userService.registerUser(email,name,null, BikeSharingUser.Role.REGULAR);
        return new OathUser(user,token);
    }
}
