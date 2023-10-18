package com.example.bike_sharing.service;

import com.example.bike_sharing.domain.User;
import com.example.bike_sharing.enums.UserServiceStatus;
import com.example.bike_sharing.model.UserCreate;
import com.example.bike_sharing.model.UserLogin;
import com.example.bike_sharing.repository.UserRepository;
import com.example.bike_sharing.validator.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }



    @Override
    public UserServiceStatus registerUser(UserCreate userRegistrationDto) {
        final String email = userRegistrationDto.getEmail();
        //nonsense email - kill the request
        if(!isUserEmailValid(email)){
            return UserServiceStatus.INVALID_USER_ARGUMENTS;
        }
        final String userName = userRegistrationDto.getUsername();
        if(userExists(email)){
            return UserServiceStatus.USER_EXISTS;
        }
        this.userRepository.save(new User(userName,email, User.Role.REGULAR));
        return UserServiceStatus.USER_CREATED;
    }

    @Override
    public UserServiceStatus loginUser(UserLogin userLoginDto) {
        return null;
    }

    @Override
    public UserServiceStatus logoutUser() {
        return null;
    }

    /**
     *
     * @param emailAddress
     * @return
     */
    private boolean userExists(String emailAddress){
        return this.userRepository.existsByEmailAddress(emailAddress);
    }

    /**
     * Method validates if email pattern is valid
     * @param email String email address provided by user
     * @return boolean true if email is valid, false otherwise
     */
    private boolean isUserEmailValid(String email){
        return EmailValidator.isValidEmailAddress(email);
    }
    @Override
    /**
     *
     * @param emailAddress
     * @return
     */
    public User fetchUserByEmail(String emailAddress){
        return this.userRepository.findUserByEmailAddress(emailAddress);
    }





}
