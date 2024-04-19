package cz.zcu.fav.kiv.authenticator.service;

import cz.zcu.fav.kiv.authenticator.token.JwtTokenProvider;
import cz.zcu.fav.kiv.authenticator.entit.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthTest {

    @Autowired
    private Auth auth;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;


}
