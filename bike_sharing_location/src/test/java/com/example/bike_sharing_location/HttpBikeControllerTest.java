package com.example.bike_sharing_location;

import com.example.bike_sharing_location.configuration.ClientAppConfiguration;
import com.example.bike_sharing_location.configuration.UserServiceConfiguration;
import com.example.bike_sharing_location.controller.BikeController;
import com.example.bike_sharing_location.model.BikeState;
import com.example.bike_sharing_location.security.WebSecurityConfig;
import com.example.bike_sharing_location.service.authentication.OAuthService;
import com.example.bike_sharing_location.service.bike.BikeService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BikeController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(WebSecurityConfig.class)
public class HttpBikeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SimpMessagingTemplate template;
    @MockBean
    private OAuthService authService;
    @MockBean
    private BikeService bikeService;

    @MockBean
    private ClientAppConfiguration clientAppConfiguration;
    @MockBean
    private UserServiceConfiguration userServiceConfiguration;

    private final String validToken = "valid_token";
    private final String invalidToken = "invalid_token";
    /**
     * integration test if server responds as excepted for valid http request
     *
     */
    @Test
    void testChangeBikeStateWithValidDataHttp() throws Exception {
        //RequestBuilder<String> builder = new HttpRequestBuilder("http://localhost:"+port+"/bike/service");


        // Create a BikeState with null bikeId
        BikeState bikeState = new BikeState();
        bikeState.setUserEmail("kocka@kocka.cz");
        bikeState.bikeId(1);
        HttpHeaders headers = new HttpHeaders();
        List<String> value = new ArrayList<>();
        value.add("Bearer "+validToken);
        headers.put("Authorization",value);
        headers.put("Content-Type", Collections.singletonList("application/json"));
        Mockito.when(authService.authenticate(validToken)).then(invocation -> ResponseEntity.ok().body("User authorized"));
        Mockito.when(bikeService.markBikeAsServiced(bikeState.getBikeId(),bikeState.getUserEmail(),"Bearer "+validToken)).then(invocation -> true);
        mockMvc.perform(put("/bike/service").content(new Gson().toJson(bikeState)).headers(headers))
                .andExpect(status().isOk());

    }

    @Test
    void testChangeBikeStateWithInvalidToken() throws Exception {
        //RequestBuilder<String> builder = new HttpRequestBuilder("http://localhost:"+port+"/bike/service");


        // Create a BikeState with null bikeId
        BikeState bikeState = new BikeState();
        bikeState.setUserEmail("kocka@kocka.cz");
        bikeState.bikeId(1);
        HttpHeaders headers = new HttpHeaders();
        List<String> value = new ArrayList<>();
        value.add("Bearer "+invalidToken);
        headers.put("Authorization",value);
        headers.put("Content-Type", Collections.singletonList("application/json"));
        Mockito.when(authService.authenticate(invalidToken)).then(invocation -> new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED));
        Mockito.when(bikeService.markBikeAsServiced(bikeState.getBikeId(),bikeState.getUserEmail(),"Bearer "+invalidToken)).then(invocation -> false);
        mockMvc.perform(put("/bike/service").content(new Gson().toJson(bikeState)).headers(headers))
                .andExpect(status().isUnauthorized());

    }

}
