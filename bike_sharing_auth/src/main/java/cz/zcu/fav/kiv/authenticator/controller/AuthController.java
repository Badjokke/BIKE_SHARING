package cz.zcu.fav.kiv.authenticator.controller;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.authenticator.dials.StatusCodes;
import cz.zcu.fav.kiv.authenticator.entit.User;
import cz.zcu.fav.kiv.authenticator.service.IAuth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoints of authenticator application
 * @version 1.0
 * @author Petr Urban, Jiri Trefil, Vaclav Hrabik
 */
@RestController
public class AuthController {

    /**
     * Instance of oAuth service
     */
    private final IAuth oAuth;

    public AuthController(IAuth oAuth){
        this.oAuth = oAuth;
    }


    /**
     * endpoint for login user
     * @param user  user with name
     * @return      ResponseEntity<String>
     *                  200 + token     - if everything is ok
     *                  401             - token creation failed
     */
    @PostMapping("/login")
    ResponseEntity<String> handleSingIn(@RequestBody User user) {
        Map<String,String> body = this.generateTokens(user);
        return ResponseEntity.status(200).body(new Gson().toJson(body));
    }

    /**
     * endpoint for authentication of user
     * @param headers   request with "Authorization" key and "Bearer token" as value in header
     * @return          ResponseEntity<String>
     *                      200 + MSG   - token is ok
     *                      401         - token is in valid
     */
    @PostMapping(value = "/authenticate", produces =  MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> authenticate(@RequestHeader HttpHeaders headers) {
        StatusCodes validationResult =  oAuth.validateToken(headers);
        return ResponseEntity.status(validationResult.getStatusCode()).body(new Gson().toJson(validationResult.getLabel()));
    }

    /**
     * endpoint of logout the user
     * @param user  user with token
     * @return      ResponseEntity<String>
     *                  200 + username      - if everything is ok
     *                  400                 - something went wrong with token
     */
    @PostMapping(value="/logout",produces = "application/json")
    ResponseEntity<String> logout(@RequestBody User user) {
        StatusCodes validationResult =  oAuth.logout(user);
        return ResponseEntity.status(validationResult.getStatusCode()).body(new Gson().toJson(validationResult.getLabel()));
    }

    /**
     * endpoint to get token with long lifespan
     * @param headers   header of request with token
     * @return          ResponseEntity<String>
     *                      200 + new token   - if everything is ok
     *                      401               - send token is in valid
     */
    @GetMapping(value = "/refresh", produces = "application/json")
    ResponseEntity<String> refreshToken(@RequestHeader HttpHeaders headers, User user) {
        Map<String,String> body = this.generateTokens(user);
        return ResponseEntity.status(200).body(new Gson().toJson(body));
    }



    private Map<String,String> generateTokens(User user){
        List<String> tokens = oAuth.generateJwt(user.getName(), true);
        Map<String,String> body = new HashMap<>();
        body.put("token",tokens.get(0));
        return body;
    }



}
