package cz.zcu.fav.kiv.authenticator.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.gson.Gson;
import com.sun.security.auth.UserPrincipal;
import cz.zcu.fav.kiv.authenticator.dials.StatusCodes;
import cz.zcu.fav.kiv.authenticator.entit.User;
import cz.zcu.fav.kiv.authenticator.http_util.HttpRequestBuilder;
import cz.zcu.fav.kiv.authenticator.http_util.RequestBuilder;
import cz.zcu.fav.kiv.authenticator.token.TokenProvider;
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
    public Auth(TokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
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
        final String GOOGLE_OAUTH_TOKEN_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token="+token;
        RequestBuilder<String> requestBuilder = new HttpRequestBuilder<>(GOOGLE_OAUTH_TOKEN_URL);
        Map<String,Object> params = new HashMap<>();
        params.put("access_token",token);
        ResponseEntity<String> response = requestBuilder.sendGetRequest(new HashMap<>(),params);
        Map<String,String> body = new Gson().fromJson(response.getBody(), Map.class);
        if(body == null || body.isEmpty() || body.containsKey("error_description")){
            return StatusCodes.USER_TOKEN_INVALID;
        }
        return StatusCodes.USER_TOKEN_VALID;
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
