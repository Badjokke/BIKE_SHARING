package com.example.bike_sharing.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class OathUser implements OAuth2User {
    private final OAuth2User oauth2User;
    private final String accessToken;
    public OathUser(OAuth2User oauth2User, String accessToken) {
        this.oauth2User = oauth2User;
        this.accessToken = accessToken;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }
    public String getEmail() {
        return oauth2User.getAttribute("email");
    }
    public String getAccessToken(){return this.accessToken;}
}
