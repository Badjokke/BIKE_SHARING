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
import java.util.logging.Logger;

/**
 *  * Implementation of controller
 *  * for endpoint description, refer to api.yaml in resources
 */
@RestController
@RequestMapping("/stand")
public class StandController implements StandApi{
    private final StandService standService;
    private final Logger logger = Logger.getLogger(StandController.class.getName());

    public StandController(StandService standService){
        this.standService = standService;
    }

    /**
     * queries all stand on map and sends them back
     * @param standId Numeric ID of the stand to get (required)
     * @return json array of stands with location and id
     */
    @Override
    @GetMapping("/location")
    public ResponseEntity<List<ObjectLocation>> fetchStands(BigDecimal standId) {
        long standIdentifier = standId.longValue();
        List<ObjectLocation> standLocations = null;
        DomainToDto<Stand,ObjectLocation> mapper = new StandToStandLocationDto();
        if(standIdentifier == 0){
            List<Stand> stands = this.standService.fetchStands();
            standLocations = mapper.mapDomainToDtos(stands);
            logger.info("Returning list of all stands. Stand count: "+standLocations.size());

            return ResponseEntity.ok(standLocations);
        }

        Stand s = this.standService.fetchStand(standIdentifier);
        if(s == null){
            logger.warning("Request for stand location failed. Stand with id "+standIdentifier+ " not found.");
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(404));
        }
        standLocations = new ArrayList<>();
        standLocations.add(mapper.mapDomainToDto(s));
        logger.info("Sending location of stand with id "+ standIdentifier);
        return ResponseEntity.ok(standLocations);
    }

}
