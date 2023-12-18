package com.example.bike_sharing_location.repository;

import com.example.bike_sharing_location.domain.Bike;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
    @Query("SELECT bike FROM Bike bike WHERE FUNCTION('TIMESTAMPDIFF', MONTH, bike.lastService, CURRENT_DATE()) < ?1 AND bike.stand.id IS NOT NULl")
    List<Bike> fetchAllBikesRideableBikes(double serviceInterval);
    @Query("SELECT bike FROM Bike bike WHERE FUNCTION('TIMESTAMPDIFF', MONTH, bike.lastService, CURRENT_DATE()) >= ?1")
    List<Bike> fetchAllBikesDueForService(double serviceInterval);
    @Query("UPDATE Bike bike SET bike.latitude = ?3, bike.longitude = ?2 WHERE bike.id = ?1")
    @Modifying
    @Transactional
    int updateBikeLocation(long bikeId, double longitude, double latitude);
    @Query("UPDATE Bike bike SET bike.stand.id = ?2 WHERE bike.id = ?1")
    @Modifying
    @Transactional
    int updateBikeStand(long bikeId, long standId);
    @Query("UPDATE Bike bike set bike.lastService = current date WHERE bike.id = ?1")
    @Transactional
    @Modifying
    int updateBikeServiceTime(long bikeId);
    @Query("UPDATE Bike bike set bike.lastService = current date WHERE bike.id in :bikeIds")
    @Transactional
    @Modifying
    int updateBikesServiceTime(List<Long> bikeIds);

    @Query("UPDATE Bike bike set bike.stand = NULL where bike.id = ?1")
    @Transactional
    @Modifying
    int removeBikeFromStand(long bikeId);
}
