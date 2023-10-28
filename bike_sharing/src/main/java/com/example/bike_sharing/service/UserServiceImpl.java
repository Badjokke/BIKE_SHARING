package com.example.bike_sharing.service;

import com.example.bike_sharing.authentication.OAuthService;
import com.example.bike_sharing.configuration.AuthConfiguration;
import com.example.bike_sharing.crypto.DefaultEncryption;
import com.example.bike_sharing.crypto.EncryptionEngine;
import com.example.bike_sharing.domain.BikeSharingUser;
import com.example.bike_sharing.enums.UserServiceStatus;
import com.example.bike_sharing.model.UserCreate;
import com.example.bike_sharing.model.UserLogin;
import com.example.bike_sharing.repository.UserRepository;
import com.example.bike_sharing.validator.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final OAuthService oAuthService;
    private final EncryptionEngine engine;
    UserServiceImpl(UserRepository userRepository, OAuthService oAuthService){
        this.oAuthService = oAuthService;
        this.userRepository = userRepository;
        this.engine = new DefaultEncryption();
    }



    @Override
    public UserServiceStatus registerUser(String email, String userName, String password ) {
        //nonsense email - kill the request
        if(!isUserEmailValid(email)){
            return UserServiceStatus.INVALID_USER_ARGUMENTS;
        }
        if(userExists(email)){
            return UserServiceStatus.USER_EXISTS;
        }
        password = engine.generateHash(password);
        this.userRepository.save(new BikeSharingUser(userName,email, BikeSharingUser.Role.REGULAR,password));
        String token = this.oAuthService.generateToken(userName,email);
        return UserServiceStatus.USER_CREATED;
    }

    @Override
    public UserServiceStatus loginUser(String email, String password) {
        BikeSharingUser user = this.userRepository.findUserByEmailAddress(email);
        if(user == null)
            return UserServiceStatus.USER_LOGIN_FAILED;

        final String passwordHash = engine.generateHash(password);
        if(!passwordHash.equals(user.getPassword()))
            return UserServiceStatus.USER_LOGIN_FAILED;

        return UserServiceStatus.USER_LOGGED_IN;
    }

    @Override
    public UserServiceStatus logoutUser(String token) {
        oAuthService.invalidateToken(token);
        return UserServiceStatus.USER_LOGGED_OUT;
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
    public BikeSharingUser fetchUserByEmail(String emailAddress){
        return this.userRepository.findUserByEmailAddress(emailAddress);
    }

    @Override
    public List<BikeSharingUser> fetchAllRegularUsers() {
        return null;
    }

    @Override
    public List<BikeSharingUser> fetchAllServiceman() {
        return null;
    }

    @Override
    public UserServiceStatus changeUserRole(BikeSharingUser.Role role) {
        return null;
    }


}
