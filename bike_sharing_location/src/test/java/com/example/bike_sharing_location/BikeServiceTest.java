package com.example.bike_sharing_location;
import com.example.bike_sharing_location.configuration.BikeServiceConfiguration;
import com.example.bike_sharing_location.domain.Bike;
import com.example.bike_sharing_location.model.BikeState;
import com.example.bike_sharing_location.model.User;
import com.example.bike_sharing_location.repository.BikeRepository;
import com.example.bike_sharing_location.repository.InMemoryBikeStorage;
import com.example.bike_sharing_location.service.bike.BikeService;
import com.example.bike_sharing_location.service.bike.BikeServiceImpl;
import com.example.bike_sharing_location.service.user.UserService;
import com.example.bike_sharing_location.socket.WebSocketConfig;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(classes = {BikeServiceImpl.class, BikeRepository.class, UserService.class,InMemoryBikeStorage.class, BikeServiceConfiguration.class})
@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ActiveProfiles("test")
public class BikeServiceTest {

    @MockBean
    private WebSocketConfig webSocketConfig;

    @Autowired
    private BikeService bikeService;

    @MockBean
    private UserService userService;

    @MockBean
    private BikeRepository bikeRepository;

    @Mock
    private InMemoryBikeStorage inMemoryBikeStorage;

    @MockBean
    private SimpMessagingTemplate template;

    @Test
    void testChangeBikeStateWithInvalidBikeId() {
        //mock user for serviceman user
        User mockUser = new User();
        mockUser
                .email("kocka@kocka.cz")
                .role(User.RoleEnum.SERVICEMAN)
                .id(1L)
                .username("kocka");

        // Create a BikeState with null bikeId
        BikeState bikeState = new BikeState();
        bikeState.setUserEmail(mockUser.getEmail());
        bikeState.bikeId(-1);

        //id will not hit anything
        Mockito.when(bikeRepository.updateBikeServiceTime(bikeState.getBikeId())).then(invocation -> 0);
        Mockito.when(userService.fetchUserInfo(mockUser.getEmail(),"")).then(invocation -> mockUser);
        //(long bikeId, String email, String token)
        boolean markedAsServiced = this.bikeService.markBikeAsServiced(bikeState.getBikeId(),bikeState.getUserEmail(),"");

        assertEquals("Bike is not marked as serviced because id is invalid",false,markedAsServiced);


        // Assert that the response is a bad request
        //assertEquals("Bad request with no bikeId", HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        //assertNull("No message in body - request is not from client app", responseEntity.getBody());
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

        Mockito.when(userService.fetchUserInfo(bikeState.getUserEmail(),""))
              .then(invocation -> mockUser);
        //return one row affected
        Mockito.when(bikeRepository.updateBikeServiceTime(bikeState.getBikeId())).then(invocation -> 1);
        // Call the controller method
        boolean updated = bikeService.markBikeAsServiced(bikeState.getBikeId(),bikeState.getUserEmail(),"");
        // Assert that the response is OK (200) and contains the expected message
        assertEquals("User is serviceman and bike id is valid",true, updated);
    }


    @Test
    void testChangeBikeStateWhenUserIsRegular() {

        // Create a BikeState with valid data
        BikeState bikeState = new BikeState();
        bikeState.setBikeId(1);
        bikeState.setUserEmail("kocka@kocka.cz");
        //mock user for serviceman user
        User mockUser = new User();
        mockUser
                .email("kocka@kocka.cz")
                .role(User.RoleEnum.REGULAR)
                .id(1L)
                .username("kocka");

        Mockito.when(userService.fetchUserInfo(bikeState.getUserEmail(),""))
                .then(invocation -> mockUser);
        //return one row affected
        Mockito.when(bikeRepository.updateBikeServiceTime(bikeState.getBikeId())).then(invocation -> 1);
        // Call the controller method
        boolean updated = bikeService.markBikeAsServiced(bikeState.getBikeId(),bikeState.getUserEmail(),"");
        // Assert that the response is OK (200) and contains the expected message
        assertEquals("user is not serviceman",false, updated);
    }

}
