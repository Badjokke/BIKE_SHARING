package com.example.bike_sharing.repository;

import com.example.bike_sharing.domain.BikeSharingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<BikeSharingUser, Integer> {
    BikeSharingUser findUserByEmailAddress(String emailAddress);
    BikeSharingUser findUserById(int userId);
    boolean existsByEmailAddress(String emailAddress);
}
