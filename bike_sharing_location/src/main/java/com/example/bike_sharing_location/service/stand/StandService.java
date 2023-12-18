package com.example.bike_sharing_location.service.stand;

import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.repository.InMemoryStandStorage;

import java.util.List;

public interface StandService {
    List<Stand> fetchStands();
    Stand fetchStand(long standId);
    Stand getCurrectStand(long standId);
    InMemoryStandStorage getInMemoryStandStorage();
}
