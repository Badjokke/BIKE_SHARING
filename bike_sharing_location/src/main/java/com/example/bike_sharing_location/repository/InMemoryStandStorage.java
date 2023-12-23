package com.example.bike_sharing_location.repository;

import com.example.bike_sharing_location.domain.Stand;

import java.util.HashMap;
import java.util.Map;

/**
 * in memory storage for stands
 * the storage is filled ad hoc - when stands are being used (ie a ride exists with that particular end stand)
 */
public class InMemoryStandStorage {
    private Map<Long, Stand> standMap;

    public InMemoryStandStorage(){
        this.standMap = new HashMap<>();
    }

    public void addStand(Stand s){
        this.standMap.putIfAbsent(s.getId(),s);
    }
    public Stand getStand(long standId){
        return this.standMap.get(standId);
    }

}
