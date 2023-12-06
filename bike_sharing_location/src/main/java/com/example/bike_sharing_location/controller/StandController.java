package com.example.bike_sharing_location.controller;

import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.mapper.DomainToDto;
import com.example.bike_sharing_location.mapper.StandToStandLocationDto;
import com.example.bike_sharing_location.model.ObjectLocation;
import com.example.bike_sharing_location.service.stand.StandService;
import com.google.gson.Gson;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stand")
public class StandController implements StandApi{
    private final StandService standService;
    public StandController(StandService standService){
        this.standService = standService;
    }

    @Override
    @GetMapping("/location")
    public ResponseEntity<List<ObjectLocation>> fetchStands(BigDecimal standId) {
        long standIdentifier = standId.longValue();
        List<ObjectLocation> standLocations = null;
        DomainToDto<Stand,ObjectLocation> mapper = new StandToStandLocationDto();
        //priznak pro vsechny stands
        if(standIdentifier == 0){
            List<Stand> stands = this.standService.fetchStands();
            standLocations = mapper.mapDomainToDtos(stands);
            return ResponseEntity.ok(standLocations);
        }

        Stand s = this.standService.fetchStand(standIdentifier);
        if(s == null){
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(404));
        }
        standLocations = new ArrayList<>();
        standLocations.add(mapper.mapDomainToDto(s));
        return ResponseEntity.ok(standLocations);
    }

}
