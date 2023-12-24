package com.example.bike_sharing_location.service.stand;

import com.example.bike_sharing_location.domain.Stand;
import com.example.bike_sharing_location.repository.InMemoryStandStorage;
import com.example.bike_sharing_location.repository.StandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class StandServiceImpl implements StandService{
    private final StandRepository standRepository;
    private final InMemoryStandStorage standStorage;

    private final Logger logger = Logger.getLogger(StandServiceImpl.class.getName());
    public StandServiceImpl(StandRepository repository){
        this.standStorage = new InMemoryStandStorage();
        this.standRepository = repository;
    }

    /**
     * Method queries all stands from database
     * @return List of all stands
     */
    @Override
    public List<Stand> fetchStands() {

        logger.info("Fetching all stands");
        return this.standRepository.findAll();
    }

    /**
     * Fetches stand with id @param standId from database
     * @param standId id of stand whose information is wanted
     * @return
     */
    @Override
    public Stand fetchStand(long standId) {
        logger.info("Fetching stand with id: "+standId);
        Optional<Stand> stand = this.standRepository.findById(standId);
        if(stand.isEmpty()){
            logger.severe("Stand with id: "+standId+" not found.");
            return null;
        }
        Stand s = stand.get();
        this.standStorage.addStand(s);
        return s;
    }




    @Override
    public InMemoryStandStorage getInMemoryStandStorage() {
        return this.standStorage;
    }
}
