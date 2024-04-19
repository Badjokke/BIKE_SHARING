package cz.zcu.fav.kiv.authenticator.service;

import cz.zcu.fav.kiv.authenticator.dials.StatusCodes;
import cz.zcu.fav.kiv.authenticator.entit.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAuth {

    StatusCodes validateToken(HttpHeaders headers);
    List<String> generateJwt(String userName, boolean refreshToken);

    StatusCodes logout(User user);

}

