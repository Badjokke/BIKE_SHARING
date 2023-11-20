package com.example.bike_sharing_location.repository;

import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
    @Query("SELECT bike FROM Bike bike WHERE (current date - bike.lastService) < ?1")
    List<Bike> fetchAllBikesNotDueForService(double serviceInterval);
    @Query("SELECT bike FROM Bike bike where (current date  - bike.lastService) >= ?1")
    List<Bike> fetchAllBikesDueForService(double serviceInterval);
    @Query("UPDATE Bike bike SET bike.latitude = :#{location.latitude}, bike.longitude = :#{location.longitude} WHERE bike.id = ?1")
    @Transactional
    @Modifying
    int updateBikeLocation(long bikeId, Location location);

    @Query("UPDATE Bike bike set bike.lastService = current date WHERE bike.id = ?1")
    @Transactional
    @Modifying
    int updateBikeServiceTime(long bikeId);
    @Query("UPDATE Bike bike set bike.lastService = current date WHERE bike.id in :#{bikeIds}")
    @Transactional
    @Modifying
    int updateBikesServiceTime(List<Long> bikeIds);

}
