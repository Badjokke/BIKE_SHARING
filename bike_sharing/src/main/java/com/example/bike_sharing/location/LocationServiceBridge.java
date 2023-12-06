package com.example.bike_sharing.location;


import com.example.bike_sharing.http_util.HttpRequestBuilder;
import com.example.bike_sharing.http_util.RequestBuilder;
import com.example.bike_sharing.model.Ride;
import com.example.bike_sharing.utils.GsonParser;
import com.example.bike_sharing.utils.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LocationServiceBridge implements BikeLocationService {
    private final String locationServiceUrl;
    public LocationServiceBridge(String url){
        this.locationServiceUrl = url;
    }




    public String getLocationServiceUrl(){
        return this.locationServiceUrl;
    }

    public List<Ride> createRideDto(List<Map<String,String>> parsedJson){
        List<Ride> rides = new ArrayList<>();
        Ride userBikeRide = new Ride();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        for(Map<String,String> jsonRide : parsedJson){
            long rideId = Long.parseLong( jsonRide.get("rideId"));
            long userId = Long.parseLong(jsonRide.get("userId"));
            long bikeId = Long.parseLong(jsonRide.get("bikeId"));
            long startStandId = Long.parseLong( jsonRide.get("startStandId"));
            long endStandId = Long.parseLong(jsonRide.get("endStandId"));
            LocalDateTime rideStart = LocalDateTime.parse(jsonRide.get("rideStart"),formatter);
            LocalDateTime rideEnd = LocalDateTime.parse(jsonRide.get("rideEnd"),formatter);
            OffsetDateTime rideStartStamp = OffsetDateTime.from(rideStart.atOffset(ZoneOffset.UTC));
            OffsetDateTime rideEndStamp = OffsetDateTime.from(rideEnd.atOffset(ZoneOffset.UTC));
            userBikeRide
                    .bikeId(bikeId)
                    .rideId(rideId)
                    .userId(userId)
                    .startStandId(startStandId)
                    .endStandId(endStandId)
                    .rideStart(rideStartStamp)
                    .rideEnd(rideEndStamp);
            rides.add(userBikeRide);
        }
        return rides;
    }

    @Override
    public List<Ride> fetchUserRides(long userId) {
        System.out.println("SENDING REQUEST TO"+this.locationServiceUrl);
        final RequestBuilder<String> requestBuilder = new HttpRequestBuilder(this.locationServiceUrl);
        Map<String,Object> body = new HashMap<>();
        body.put("userId", String.valueOf(userId));
        ResponseEntity<String> entity = requestBuilder.sendGetRequest(new HashMap<>(),body);
        //invalid userId
        if(entity.getStatusCode().is4xxClientError()){
            return null;
        }
        String userRidesJson = entity.getBody();
        JsonParser<List<Map<String,String>>> parser = new GsonParser<>(new TypeToken<List<Map<String,String>>>(){}.getType());
        List<Map<String,String>> parsedJson = parser.parseJson(userRidesJson);
        //List<Ride> userRides = this.parseUserRides(userRidesJson);
        return this.createRideDto(parsedJson);

    }
}
