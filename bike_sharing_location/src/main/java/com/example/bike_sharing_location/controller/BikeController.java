package com.example.bike_sharing_location.controller;
import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.mapper.BikeToBikeLocationDto;
import com.example.bike_sharing_location.mapper.DomainToDto;
import com.example.bike_sharing_location.model.ObjectLocation;
import com.example.bike_sharing_location.model.StompBikeLocationMessageWrapper;
import com.example.bike_sharing_location.model.StompBikeRideMessageWrapper;
import com.example.bike_sharing_location.service.bike.BikeService;
import com.google.gson.Gson;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/bike")
public class BikeController implements BikeApi {
    private final BikeService bikeService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public BikeController(BikeService bikeService, SimpMessagingTemplate simpMessagingTemplate){
        this.bikeService = bikeService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/bike")
    public void fetchBikeLocations(StompBikeLocationMessageWrapper bikeLocationMessage) {
        long bikeId = bikeLocationMessage.getBikeId();
        DomainToDto<Bike,ObjectLocation> mapper = new BikeToBikeLocationDto();
        String json;
        if(bikeId == 0){
            List<Bike> bikes = this.bikeService.getAllCurrentBikes();
            List<ObjectLocation>  locations = mapper.mapDomainToDtos(bikes);
            json = new Gson().toJson(locations);
        }
        else {
            Bike subscribedbike = this.bikeService.getCurrentBike(bikeId);
            ObjectLocation location = mapper.mapDomainToDto(subscribedbike);
            json = new Gson().toJson(location);
        }
        String communicationChannel = "/bike_location/location/"+bikeId;

        this.simpMessagingTemplate.convertAndSend(communicationChannel,json);
    }



}
