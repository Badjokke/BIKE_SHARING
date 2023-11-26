package com.example.bike_sharing_location.service.stand;

import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.repository.InMemoryStandStorage;
import com.example.bike_sharing_location.repository.StandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class StandServiceImpl implements StandService{
    private final StandRepository standRepository;
    private final InMemoryStandStorage standStorage;
    public StandServiceImpl(StandRepository repository){
        this.standStorage = new InMemoryStandStorage();
        this.standRepository = repository;
    }
    @Override
    public List<Stand> fetchStands() {
        return this.standRepository.findAll();
    }

    @Override
    public Stand fetchStand(long standId) {
        Optional<Stand> stand = this.standRepository.findById(standId);
        if(stand.isEmpty())
            return null;
        Stand s = stand.get();
        this.standStorage.addStand(s);
        return s;
    }

    @Override
    public InMemoryStandStorage getInMemoryStandStorage() {
        return this.standStorage;
    }
}
