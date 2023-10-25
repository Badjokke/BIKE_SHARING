package com.example.bike_sharing.authentication;

import com.example.bike_sharing.domain.BikeSharingUser;
import com.example.bike_sharing.http_util.HttpRequestBuilder;
import com.example.bike_sharing.http_util.RequestBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OAuthServiceImpl implements OAuthService, UserDetailsService {

    @Override
    public String generateToken(String username, String userEmail) {
        BikeSharingUser user = new BikeSharingUser(username,userEmail);
        RequestBuilder<String> requestBuilder = new HttpRequestBuilder<>();
        return null;
    }

    @Override
    public void invalidateToken(String token) {

    }

    @Override
    public ResponseEntity<String> authenticate(String token) {
        return ResponseEntity.status(200).body("{\"token\":\"ahoj_svete\"}");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "",new ArrayList<>());
    }
}
