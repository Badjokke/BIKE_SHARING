package com.example.bike_sharing.service.user;

import com.example.bike_sharing.mappers.BikeUserToUser;
import com.example.bike_sharing.mappers.ModelToDto;
import com.example.bike_sharing.model.Ride;
import com.example.bike_sharing.model.User;
import com.example.bike_sharing.service.authentication.OAuthService;
import com.example.bike_sharing.crypto.DefaultEncryption;
import com.example.bike_sharing.crypto.EncryptionEngine;
import com.example.bike_sharing.domain.BikeSharingUser;
import com.example.bike_sharing.enums.UserServiceStatus;
import com.example.bike_sharing.repository.UserRepository;
import com.example.bike_sharing.service.location.LocationService;
import com.example.bike_sharing.validator.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final OAuthService oAuthService;
    private final EncryptionEngine engine;
    private final LocationService locationService;
    UserServiceImpl(UserRepository userRepository, OAuthService oAuthService, LocationService locationService){
        this.oAuthService = oAuthService;
        this.userRepository = userRepository;
        this.engine = new DefaultEncryption();
        this.locationService = locationService;
    }



    @Override
    public String registerUser(String email, String userName, String password,BikeSharingUser.Role role ) {
        //nonsense email - kill the request
        if(!isUserEmailValid(email)){
            return null;
        }
        if(userExists(email)){
            return null;
        }
        password = engine.generateHash(password);
        this.userRepository.save(new BikeSharingUser(userName,email, role,password));
        return this.oAuthService.generateToken(userName,email);
    }

    @Override
    public Map<String,String> loginUser(String email, String password) {
        if(email == null || password == null){
            return null;
        }
        BikeSharingUser user = this.userRepository.findUserByEmailAddress(email);
        if(user == null)
            return null;

        final String passwordHash = engine.generateHash(password);
        if(!passwordHash.equals(user.getPassword()))
            return null;
        String token = this.oAuthService.generateToken(user.getName(),email);
        String userRole = user.getRole().getValue();
        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("token",token);
        userInfo.put("role",userRole);
        return userInfo;
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
     * @param emailAddress
     * @return
     */
    public BikeSharingUser fetchUserByEmail(String emailAddress){
        return this.userRepository.findUserByEmailAddress(emailAddress);
    }
    private List<BikeSharingUser> fetchUsers(BikeSharingUser.Role role){
       return this.userRepository.findBikeSharingUserByRole(role);
    }
    @Override
    public List<User> fetchAllRegularUsers() {
        ModelToDto<BikeSharingUser,User> dtoMapper = new BikeUserToUser();
        List<BikeSharingUser> bikeSharingUsers = this.fetchUsers(BikeSharingUser.Role.REGULAR);
        return dtoMapper.mapToDto(bikeSharingUsers);
    }

    @Override
    public List<User> fetchAllServiceman() {
        ModelToDto<BikeSharingUser,User> dtoMapper = new BikeUserToUser();
        List<BikeSharingUser> bikeSharingUsers = this.fetchUsers(BikeSharingUser.Role.SERVICEMAN);
        return dtoMapper.mapToDto(bikeSharingUsers);
    }

    @Override
    public List<Ride> fetchUserRides(String userEmail, String authorization) {
        BikeSharingUser user = this.userRepository.findUserByEmailAddress(userEmail);
        if(user == null){
            return null;
        }
        return locationService.fetchUserRides(user.getId(),authorization);
    }

    @Override
    public BikeSharingUser fetchUserInfo(String userEmail) {
        return this.userRepository.findUserByEmailAddress(userEmail);
    }

    @Override
    public UserServiceStatus changeUserRole(String userEmail, BikeSharingUser.Role role) {
        if(userEmail == null || role == null){
            return UserServiceStatus.INVALID_CHANGE_ROLE_ARGUMENTS;
        }
        BikeSharingUser userInfo = this.fetchUserByEmail(userEmail);
        if(userInfo.getRole() == role){
            return UserServiceStatus.USER_ROLE_CHANGED;
        }
        int rowsAffected = this.userRepository.changeUserRole(userInfo.getId(),role);
        if(rowsAffected == 0){
            return UserServiceStatus.ROLE_CHANGE_FAILED;
        }

        return UserServiceStatus.USER_ROLE_CHANGED;
    }





}
