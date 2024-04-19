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
        for(Map<String,String> jsonRide : parsedJson){
            Ride userBikeRide = new Ride();
            long rideId = Long.parseLong( jsonRide.get("rideId"));
            long userId = Long.parseLong(jsonRide.get("userId"));
            long bikeId = Long.parseLong(jsonRide.get("bikeId"));
            long startStandId = Long.parseLong( jsonRide.get("startStandId"));
            long endStandId = Long.parseLong(jsonRide.get("endStandId"));
            DateTimeFormatter tmp = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

            // Parse the string to OffsetDateTime
            OffsetDateTime rideStartStamp = OffsetDateTime.parse(jsonRide.get("rideStart"), tmp);
            OffsetDateTime rideEndStamp = OffsetDateTime.parse(jsonRide.get("rideEnd"), tmp);
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
    public List<Ride> fetchUserRides(long userId, String authorization) {
        System.out.println("SENDING REQUEST TO"+this.locationServiceUrl);
        final RequestBuilder<String> requestBuilder = new HttpRequestBuilder(this.locationServiceUrl);
        Map<String,Object> body = new HashMap<>();
        body.put("userId", String.valueOf(userId));
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",authorization);
        ResponseEntity<String> entity = requestBuilder.sendGetRequest(headers,body);
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
