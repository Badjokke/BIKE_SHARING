package com.example.bike_sharing_location;

import com.example.bike_sharing_location.configuration.BikeServiceConfiguration;
import com.example.bike_sharing_location.configuration.ClientAppConfiguration;
import com.example.bike_sharing_location.configuration.UserServiceConfiguration;
import com.example.bike_sharing_location.controller.BikeController;
import com.example.bike_sharing_location.model.BikeState;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.BikeRepository;
import com.example.bike_sharing_location.security.WebSecurityConfig;
import com.example.bike_sharing_location.service.bike.BikeService;
import com.example.bike_sharing_location.service.bike.BikeServiceImpl;
import com.example.bike_sharing_location.service.user.UserService;
import com.example.bike_sharing_location.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(classes = {BikeServiceImpl.class, BikeRepository.class, UserServiceImpl.class, BikeServiceConfiguration.class,UserServiceConfiguration.class})
//BikeSharingApplication microservice must be running on local cluster before running tests
public class ServiceCommunicationTest {
    @Autowired
    private BikeService bikeService;
    @MockBean
    private BikeRepository bikeRepository;
    //test if my service can fetch user info for my dummy serviceman user
    @Test
    void markBikeAsServicedValidUserRole(){
        // Create a BikeState with null bikeId
        BikeState bikeState = new BikeState();
        bikeState.setUserEmail("kocka@kocka.cz");
        bikeState.bikeId(1);

        Mockito.when(bikeRepository.updateBikeServiceTime(bikeState.getBikeId())).then(invocation -> 1);
        boolean markedAsServiced = this.bikeService.markBikeAsServiced(bikeState.getBikeId(),bikeState.getUserEmail(),"Bearer sample_token");

        assertEquals("Bike is not marked as serviced because id is invalid",true,markedAsServiced);
    }

    @Test
    void markBikeAsServicedInvalidUserRole(){
        BikeState bikeState = new BikeState();
        bikeState.setUserEmail("pes@pes.cz");
        bikeState.bikeId(1);

        Mockito.when(bikeRepository.updateBikeServiceTime(bikeState.getBikeId())).then(invocation -> 1);
        boolean markedAsServiced = this.bikeService.markBikeAsServiced(bikeState.getBikeId(),bikeState.getUserEmail(),"Bearer sample_token");

        assertEquals("Bike is not marked as serviced because user is not a serviceman.",false,markedAsServiced);
    }

    @Test
    void markBikeAsServicedInvalidUser(){
        BikeState bikeState = new BikeState();
        bikeState.setUserEmail("idont@exist.com");
        bikeState.bikeId(1);

        Mockito.when(bikeRepository.updateBikeServiceTime(bikeState.getBikeId())).then(invocation -> 1);
        boolean markedAsServiced = this.bikeService.markBikeAsServiced(bikeState.getBikeId(),bikeState.getUserEmail(),"Bearer sample_token");

        assertEquals("User doesnt exist - cant service the bike.",false,markedAsServiced);
    }


}
