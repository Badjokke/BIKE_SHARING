package com.example.bike_sharing_location;

import com.example.bike_sharing_location.configuration.BikeServiceConfiguration;
import com.example.bike_sharing_location.controller.BikeController;
import com.example.bike_sharing_location.model.BikeState;
import com.example.bike_sharing_location.model.ChangeBikeState200Response;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.BikeRepository;
import com.example.bike_sharing_location.service.bike.BikeService;
import com.example.bike_sharing_location.service.bike.BikeServiceImpl;
import com.example.bike_sharing_location.service.user.UserLocationService;
import com.example.bike_sharing_location.service.user.UserService;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNull;

@SpringBootTest
public class BikeControllerTest {

    @MockBean
    private BikeRepository bikeRepository;

    @Mock
    private UserLocationService userLocationService;

    @Autowired
    private BikeController controller;



    @Test
    void testChangeBikeStateWithNullBikeId() {

        // Create a BikeState with null bikeId
        BikeState bikeState = new BikeState();
        bikeState.setUserEmail("hello@world.com");

        // Call the controller method
        ResponseEntity<ChangeBikeState200Response> responseEntity = controller.changeBikeState("", bikeState);

        // Assert that the response is a bad request
        assertEquals("Bad request with no bikeId", 400, responseEntity.getBody());
        assertNull("No message in body - request is not from client app", responseEntity.getBody());
    }

    @Test
    void testChangeBikeStateWithValidData() {

        // Create a BikeState with valid data
        BikeState bikeState = new BikeState();
        bikeState.setBikeId(1);
        bikeState.setUserEmail("kocka@kocka.cz");
        //mock user for serviceman user
        User mockUser = new User();
        mockUser
                .email("kocka@kocka.cz")
                .role(User.RoleEnum.SERVICEMAN)
                .id(1L)
                .username("kocka");

        Mockito.when(userLocationService.fetchUserInfo("kocka@kocka.cz",""))
                .then(invocation -> mockUser);
        //return one row affected
        Mockito.when(bikeRepository.updateBikeServiceTime(1L)).then(invocation -> 1);
        // Call the controller method
        ResponseEntity<ChangeBikeState200Response> responseEntity = controller.changeBikeState("", bikeState);

        // Assert that the response is OK (200) and contains the expected message
        assertEquals("Bike is correctly marked as serviced.",200, responseEntity.getStatusCodeValue());
        assertEquals("Controller response is correct.","Bike with id: 1 marked as serviced", responseEntity.getBody().getMessage());
    }

    @Test
    void testChangeBikeStateWithInvalidUserRole() {

        // Create a BikeState with invalid userEmail
        BikeState bikeState = new BikeState();
        bikeState.setBikeId(1);
        bikeState.setUserEmail("tonda@tonda.cz");
        User mockUser = new User();
        mockUser
                .email("tonda@tonda.cz")
                .role(User.RoleEnum.REGULAR)
                .id(1L)
                .username("tonda");
        Mockito.when(userLocationService.fetchUserInfo("tonda@tonda.cz",""))
                .then(invocation -> mockUser);
        // Call the controller method
        ResponseEntity<ChangeBikeState200Response> responseEntity = controller.changeBikeState("", bikeState);

        // Assert that the response is a bad request
        assertEquals("User is not serviceman.",400, responseEntity.getStatusCodeValue());
        assertNull("No message in response body",responseEntity.getBody());
    }


}
