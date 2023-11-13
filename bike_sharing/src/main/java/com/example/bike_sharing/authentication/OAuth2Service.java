package com.example.bike_sharing.authentication;

import com.example.bike_sharing.model.OathUser;
import com.example.bike_sharing.service.UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Service extends DefaultOAuth2UserService {
    private final UserService userService;
    private final OAuthService oAuthService;
    public OAuth2Service(UserService userService, OAuthService oAuthService){
        this.userService = userService;
        this.oAuthService = oAuthService;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        String token = userRequest.getAccessToken().getTokenValue();
        oAuthService.authenticate(token);
        return new OathUser(user);
    }
}
