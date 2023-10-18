package com.example.bike_sharing.authentication;

import com.example.bike_sharing.domain.User;
import com.example.bike_sharing.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class OAuthServiceImpl implements OAuthService, UserDetailsService {
    private final UserService userService;
    public OAuthServiceImpl(UserService userService){
        this.userService = userService;
    }
    @Override
    public String generateToken(String username, String userEmail) {
        return null;
    }

    @Override
    public void invalidateToken(String token) {

    }

    @Override
    public boolean authenticate(String token) {
        return false;
    }
    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        final User user = this.userService.fetchUserByEmail(emailAddress);
        return new org.springframework.security.core.userdetails.User(user.getName(), "",new ArrayList<>());
    }
}
