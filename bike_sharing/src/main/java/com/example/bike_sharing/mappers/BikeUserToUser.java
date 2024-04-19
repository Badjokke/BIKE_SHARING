package com.example.bike_sharing.mappers;

import com.example.bike_sharing.domain.BikeSharingUser;
import com.example.bike_sharing.model.User;

import java.util.ArrayList;
import java.util.List;

public class BikeUserToUser implements ModelToDto<BikeSharingUser, User> {
    @Override
    public User mapToDto(BikeSharingUser modelObject) {
        User user = new User();
        user.setEmail(modelObject.getEmailAddress());
        user.setId(modelObject.getId());
        user.setRole(User.RoleEnum.fromValue(modelObject.getRole().getValue()));
        user.setUsername(modelObject.getName());
        return user;
    }

    @Override
    public List<User> mapToDto(List<BikeSharingUser> modelObjects) {
        List<User> users = new ArrayList<>();
        for(BikeSharingUser u : modelObjects)
            users.add(mapToDto(u));
        return users;
    }
}
