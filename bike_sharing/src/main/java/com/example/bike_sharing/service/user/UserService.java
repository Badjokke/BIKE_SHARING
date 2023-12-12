package com.example.bike_sharing.service.user;

import com.example.bike_sharing.domain.BikeSharingUser;
import com.example.bike_sharing.domain.BikeSharingUser.Role;

import com.example.bike_sharing.enums.UserServiceStatus;
import com.example.bike_sharing.model.Ride;
import com.example.bike_sharing.model.User;
import com.example.bike_sharing.model.UserCreate;
import com.example.bike_sharing.model.UserLogin;

import java.util.List;

public interface UserService {


    String registerUser(String email, String userName, String password,BikeSharingUser.Role role);

    String loginUser(String email, String password);
    UserServiceStatus logoutUser(String token);
    BikeSharingUser fetchUserByEmail(String emailAddress);

    List<User> fetchAllRegularUsers();
    List<User> fetchAllServiceman();

    List<Ride> fetchUserRides(String userEmail);
    BikeSharingUser fetchUserInfo(String userEmail);
    UserServiceStatus changeUserRole(String userEmail, Role role);

}
