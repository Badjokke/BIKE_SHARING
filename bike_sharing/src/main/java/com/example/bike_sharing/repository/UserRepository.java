package com.example.bike_sharing.repository;

import com.example.bike_sharing.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByEmailAddress(String emailAddress);
    boolean existsByEmailAddress(String emailAddress);
}
