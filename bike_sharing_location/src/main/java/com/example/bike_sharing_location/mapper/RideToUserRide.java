package com.example.bike_sharing_location.mapper;

import com.example.bike_sharing_location.domain.Ride;
import com.example.bike_sharing_location.model.UserRide;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RideToUserRide implements DomainToDto<Ride, UserRide> {
    OffsetDateTime convertDateToOffsetDateTime(Date date){
        Instant instant = date.toInstant();

        // Specify a time zone offset, for example, UTC
        ZoneOffset offset = ZoneOffset.UTC;

        // Create OffsetDateTime from Instant and time zone offset
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(instant, offset);
        return offsetDateTime;
    }


    @Override
    public UserRide mapDomainToDto(Ride modelObject) {
        UserRide userRide = new UserRide();

        Date start = modelObject.getStartTimestamp();
        Date end = modelObject.getEndTimestamp();

        OffsetDateTime rideStart = convertDateToOffsetDateTime(start);
        OffsetDateTime rideEnd = convertDateToOffsetDateTime(end);

        userRide
                .userId(modelObject.getUserId())
                .rideId(modelObject.getId())
                .bikeId(modelObject.getBike().getId())
                .endStandId(modelObject.getEndStand().getId())
                .startStandId(modelObject.getStartStand().getId())
                .rideStart(rideStart)
                .rideEnd(rideEnd);
        return userRide;
    }

    @Override
    public List<UserRide> mapDomainToDtos(List<Ride> modelObjects) {
        List<UserRide> userRides = new ArrayList<>();
        for(Ride ride : modelObjects)
            userRides.add(this.mapDomainToDto(ride));
        return userRides;
    }
}
