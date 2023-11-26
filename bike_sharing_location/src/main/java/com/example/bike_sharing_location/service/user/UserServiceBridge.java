package com.example.bike_sharing_location.service.user;

import com.example.bike_sharing_location.configuration.UserServiceConfiguration;
import com.example.bike_sharing_location.http_util.HttpRequestBuilder;
import com.example.bike_sharing_location.http_util.RequestBuilder;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.utils.GsonParser;
import com.example.bike_sharing_location.utils.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceBridge implements UserLocationService{
    private final UserServiceConfiguration userServiceConfiguration;
    public UserServiceBridge(UserServiceConfiguration userServiceConfiguration){
        this.userServiceConfiguration = userServiceConfiguration;
    }

    private User createUserDto(Map<String,String> parsedJson){
        long id = Long.parseLong(parsedJson.get("id"));
        String username = parsedJson.get("username");
        String email = parsedJson.get("email");
        User.RoleEnum role = User.RoleEnum.valueOf(parsedJson.get("role"));
        User user = new User();
        user
                .id(id)
                .username(username)
                .email(email)
                .role(role);
        return user;
    }

    @Override
    public User fetchUserInfo(String email) {
        final String userInfoUrl = this.userServiceConfiguration.getUSER_INFO_URL();
        final RequestBuilder<String> requestBuilder = new HttpRequestBuilder(userInfoUrl);
        Map<String,Object> body = new HashMap<>();
        body.put("userEmail", String.valueOf(email));
        ResponseEntity<String> entity = requestBuilder.sendGetRequest(new HashMap<>(),body);
        //invalid userId
        if(entity.getStatusCode().is4xxClientError()){
            return null;
        }
        String userRidesJson = entity.getBody();
        JsonParser<Map<String,String>> parser = new GsonParser<>(new TypeToken<Map<String,String>>(){}.getType());
        Map<String,String> parsedJson = parser.parseJson(userRidesJson);
        //List<Ride> userRides = this.parseUserRides(userRidesJson);
        return this.createUserDto(parsedJson);
    }
}
