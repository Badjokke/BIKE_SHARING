package com.example.bike_sharing_location.repository;

import com.example.bike_sharing_location.domain.Stand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandRepository extends JpaRepository<Stand,Long> {
}
