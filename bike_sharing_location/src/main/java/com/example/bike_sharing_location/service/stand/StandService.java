package com.example.bike_sharing_location.service.stand;

import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.repository.InMemoryStandStorage;

import java.util.List;

public interface StandService {
    /**
     * queries db for stands
     * @return list of all stands in database
     */
    List<Stand> fetchStands();

    /**
     * fetches stand with id @param standId
     * @param standId id of stand
     * @return Stand object or null if stand is not in db
     */
    Stand fetchStand(long standId);

    /**
     * getter for memory storage of stands
     * @return in memory storage for stands
     */
    InMemoryStandStorage getInMemoryStandStorage();
}
