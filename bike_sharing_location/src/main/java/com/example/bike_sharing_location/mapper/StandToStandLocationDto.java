package com.example.bike_sharing_location.mapper;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.model.ObjectLocation;
import com.example.bike_sharing_location.model.ObjectLocationLocation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StandToStandLocationDto implements DomainToDto<Stand,ObjectLocation>{
    @Override
    public List<ObjectLocation> mapDomainToDtos(List<Stand> from) {
        List<ObjectLocation> result = new ArrayList<>();
        for(Stand b : from){
            result.add(mapDomainToDto(b));
        }
        return result;
    }

    @Override
    public ObjectLocation mapDomainToDto(Stand from) {
        ObjectLocation dto = new ObjectLocation();
        ObjectLocationLocation location = new ObjectLocationLocation();
        location.setLatitude(BigDecimal.valueOf(from.getLatitude()));
        location.setLongitude(BigDecimal.valueOf(from.getLongitude()));
        dto.setId(from.getId());
        dto.setLocation(location);
        return dto;
    }
}
