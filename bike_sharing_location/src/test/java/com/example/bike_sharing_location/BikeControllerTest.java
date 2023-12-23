package com.example.bike_sharing_location;

import com.example.bike_sharing_location.configuration.BikeServiceConfiguration;
import com.example.bike_sharing_location.controller.BikeController;
import com.example.bike_sharing_location.http_util.HttpRequestBuilder;
import com.example.bike_sharing_location.http_util.RequestBuilder;
import com.example.bike_sharing_location.model.BikeState;
import com.example.bike_sharing_location.model.ChangeBikeState200Response;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.BikeRepository;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;
import com.example.bike_sharing_location.repository.RideRepository;
import com.example.bike_sharing_location.security.JwtAuthFilter;
import com.example.bike_sharing_location.service.authentication.OAuthService;
import com.example.bike_sharing_location.service.bike.BikeService;
import com.example.bike_sharing_location.service.bike.BikeServiceImpl;
import com.example.bike_sharing_location.service.ride.RideService;
import com.example.bike_sharing_location.service.user.UserLocationService;
import com.example.bike_sharing_location.service.user.UserService;
import com.example.bike_sharing_location.socket.WebSocketConfig;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNull;

@SpringBootTest(classes = {BikeService.class,BikeController.class ,BikeRepository.class})
@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ActiveProfiles("test")
public class BikeControllerTest {


    @MockBean
    private BikeService bikeService;


    @Autowired
    private BikeController controller;

    @MockBean
    private SimpMessagingTemplate template;



    @Test
    void testChangeBikeStateWithNullBikeId() {

        // Create a BikeState with null bikeId
        BikeState bikeState = new BikeState();
        bikeState.setUserEmail("hello@world.com");
        // Call the controller method
        ResponseEntity<ChangeBikeState200Response> responseEntity = controller.changeBikeState("", bikeState);

        // Assert that the response is a bad request
        assertEquals("Bad request with no bikeId", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

        //Mockito.when(userLocationService.fetchUserInfo("kocka@kocka.cz",""))
          //      .then(invocation -> mockUser);
        //return one row affected
        //Mockito.when(bikeRepository.updateBikeServiceTime(1L)).then(invocation -> 1);
        Mockito.when(bikeService.markBikeAsServiced(bikeState.getBikeId(),bikeState.getUserEmail(),"")).then(invocation -> true);
        // Call the controller method
        ResponseEntity<ChangeBikeState200Response> responseEntity = controller.changeBikeState("", bikeState);

        // Assert that the response is OK (200) and contains the expected message
        assertEquals("Bike is correctly marked as serviced.",HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Controller response is correct.","Bike with id: 1 marked as serviced", responseEntity.getBody().getMessage());
    }



}