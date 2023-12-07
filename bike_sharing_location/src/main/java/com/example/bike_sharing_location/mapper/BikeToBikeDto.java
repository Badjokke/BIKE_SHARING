package com.example.bike_sharing_location.mapper;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.model.BikeDto;
import com.example.bike_sharing_location.model.ObjectLocationLocation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BikeToBikeDto implements DomainToDto<Bike, BikeDto> {

    @Override
    public List<BikeDto> mapDomainToDtos(List<Bike> from) {
        List<BikeDto> dtos = new ArrayList<>();
        for(Bike b : from)
            dtos.add(mapDomainToDto(b));
        return dtos;
    }

    @Override
    public BikeDto mapDomainToDto(Bike from) {
        BikeDto dto = new BikeDto();
        Stand bikeStand = from.getStand();
        Long standId = bikeStand != null? bikeStand.getId() :  null;

        dto
                .id(from.getId())
                .bikeStandId(standId);
        return dto;
    }
}
