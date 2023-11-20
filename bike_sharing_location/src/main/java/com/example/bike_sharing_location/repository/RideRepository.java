package com.example.bike_sharing_location.repository;

import com.example.bike_sharing_location.domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride,Long> {
    @Query("SELECT ride FROM Ride ride WHERE ride.userId = ?1 ORDER BY ride.startTimestamp ASC")
    List<Ride> fetchUserRides(long userId);
}
