package cz.zcu.fav.kiv.authenticator.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.sun.security.auth.UserPrincipal;
import cz.zcu.fav.kiv.authenticator.dials.StatusCodes;
import cz.zcu.fav.kiv.authenticator.token.JwtTokenProvider;
import cz.zcu.fav.kiv.authenticator.entit.User;
import cz.zcu.fav.kiv.authenticator.token.TokenProvider;
import cz.zcu.fav.kiv.authenticator.utils.JSONBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * OAuth service
 * @version 1.0
 * @author Petr Urban, Jiri Trefil, Vaclav Hrabik
 */
@Service
public class Auth implements IAuth {

    /**
     * Class which manage JWT token
     */
    private final TokenProvider jwtTokenProvider;
    private final HttpTransport httpTransport;
    private final JsonFactory JsonFactory ;
    public Auth(TokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
        this.httpTransport = new NetHttpTransport();
        this.JsonFactory = GsonFactory.getDefaultInstance();
    }


    /**
     * TODO verifikace refresh tokenu
     * Method to call validation of JWT token
     * @param headers    header of request for authentication
     * @return              ResponseEntity<String>
     *                          200 + MSG   - token is ok
     *                          401         - token is in valid
     */
    @Override
    public StatusCodes validateToken(HttpHeaders headers){
        String authHeaders = headers.getFirst(HttpHeaders.AUTHORIZATION);
        final String GOOGLE_TOKEN_PREFIX = "ya29";
        if (authHeaders == null) {
            return StatusCodes.USER_TOKEN_INVALID;
        }
        String token = authHeaders.replace("Bearer ", "");
        if(token.startsWith(GOOGLE_TOKEN_PREFIX)){
            try{
                return this.validateGoogleToken(token);
            }
            catch (GeneralSecurityException | IOException exception){
                return StatusCodes.USER_TOKEN_INVALID;
            }
        }
        String name = jwtTokenProvider.getNameFromToken(token);

        if (name == null) {
            return StatusCodes.USER_TOKEN_INVALID;
        }
        return jwtTokenProvider.validateToken(token,false);
    }

    private StatusCodes validateGoogleToken(String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(this.httpTransport,this.JsonFactory).setAudience(List.of("286479622083-89aferh4mb7o51r4sbtsbcekf18177c6.apps.googleusercontent.com")).build();
        GoogleIdToken googleToken = verifier.verify(token);
        if(googleToken != null){
            return StatusCodes.USER_TOKEN_VALID;
        }
        return StatusCodes.USER_TOKEN_INVALID;
    }

    /**
     * Method to call generation of new JWT token
     * @param userName name of the user that logged in and requires token
     * @return      ResponseEntity<String>
     *                  200 + token     - if everything is ok
     *                  401             - token creation failed
     */
    @Override
    public List<String> generateJwt(String userName, boolean refreshToken) {
        UserPrincipal userPrincipal = new UserPrincipal(userName);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, "", null);
        return jwtTokenProvider.generateToken(authentication,refreshToken);
    }

    /**
     * Method to call invalidation of users JWT token
     * @param user  User who wants to log out
     * @return      ResponseEntity<String>
     *                  200 + username      - if everything is ok
     *                  400                 - something went wrong with token
     */
    @Override
    public StatusCodes logout(User user){
        String token = user.getToken();
        if(token == null || token.isEmpty()){
            return StatusCodes.USER_TOKEN_INVALID;
        }
        jwtTokenProvider.invalidateToken(token);
        return StatusCodes.USER_LOGGED_OUT;
    }

}
