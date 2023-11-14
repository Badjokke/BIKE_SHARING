package com.example.bike_sharing.repository;

import com.example.bike_sharing.domain.BikeSharingUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<BikeSharingUser, Integer> {
    BikeSharingUser findUserByEmailAddress(String emailAddress);
    BikeSharingUser findUserById(int userId);
    List<BikeSharingUser> findBikeSharingUserByRole(BikeSharingUser.Role role);
    boolean existsByEmailAddress(String emailAddress);
    @Modifying
    @Transactional
    @Query("UPDATE BikeSharingUser User SET User.role = ?2 WHERE User.id = ?1")
    int changeUserRole(Long userId, BikeSharingUser.Role role);





}
