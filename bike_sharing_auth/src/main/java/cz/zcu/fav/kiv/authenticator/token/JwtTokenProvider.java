package cz.zcu.fav.kiv.authenticator.token;

import cz.zcu.fav.kiv.authenticator.dials.JwtExceptionStatus;
import cz.zcu.fav.kiv.authenticator.dials.StatusCodes;
import cz.zcu.fav.kiv.authenticator.jwtException.JwtException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import com.sun.security.auth.UserPrincipal;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class that has methods for JWT token management
 * @version 1.1
 * @author Petr Urban, Jiri Trefil, Vaclav Hrabik
 */
@Component
public class JwtTokenProvider implements TokenProvider {

    /**
     * Key for JWT token
     */
    @Value("${secret.key}")
    private String JWT_SECRET;
    @Value("${secret.refresh.key}")
    private String JWT_REFRESH_SECRET;

    /**
     * Life spawn of token, now 10 min
     */
    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private long JWT_EXPIRATION; // 600 sec = 10 min
    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private long REFRESH_TOKEN_EXPIRATION;
    /**
     * Life spawn of refreshed token, 1 hour
     */
    @Value("${EXTENDED_ACCESS_TOKEN_EXPIRATON}")
    private long JWT_EXPIRATION_EXTENDED; // 3600 sec = 60 min
    /**
     * method to generate JWT token from username
     * @param authentication    wrapper of user credentials
     * @return                  JWT token as string
     */
    public List<String> generateToken(Authentication authentication, boolean refreshToken) {
        List<String> tokens = new ArrayList<>();
        long expiration = refreshToken? JWT_EXPIRATION_EXTENDED : JWT_EXPIRATION;
        String accessJwtToken = this.generateAccessToken(authentication,expiration);
        String refreshJwtToken = this.generateRefreshToken(authentication);
        tokens.add(accessJwtToken);
        tokens.add(refreshJwtToken);
        return tokens;
    }

    private String generateAccessToken(Authentication authentication, long expiration){
        return generateJwtToken(authentication,expiration, JWT_SECRET);
    }

    private String generateRefreshToken(Authentication authentication){
       return generateJwtToken(authentication,REFRESH_TOKEN_EXPIRATION,JWT_REFRESH_SECRET);

    }
    private String generateJwtToken(Authentication authentication, long expiration, String privateKey){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        String randomId = UUID.randomUUID().toString();
        return Jwts.builder()
                .setId(randomId)
                .setSubject(userPrincipal.getName())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, privateKey)
                .compact();
    }


    /**
     * method for validation of JWT token
     * @param token     JWT token for validation
     * @return          Code value of StatusCodes
     *                      - 200 + MSG - if token is valid
     *                      - 401 + MSG - if something is wrong with token
     */
    @Override
    public StatusCodes validateToken(String token, boolean isRefreshToken) {

        // controls token it self
        try {
            parserJWTToken(token,isRefreshToken?JWT_REFRESH_SECRET:JWT_SECRET);
            return StatusCodes.USER_TOKEN_VALID;
        } catch (JwtException e) {
            // invalid signature
            return StatusCodes.USER_TOKEN_INVALID;
        }

    }

    /**
     * Internal method to parse JWT token in one place
     * @param token         token for parsing
     * @return              parsed token
     * @throws Exception    generic exception - everytime it must be handled differently
     */
    private Jws<Claims> parserJWTToken(String token, String signingKey) throws JwtException {
        //no token is provided
        if(token == null || token.length() == 0)return null;
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
        } catch (SignatureException ex) {
            // invalid signature
            throw new JwtException(JwtExceptionStatus.INVALID_SIGNATURE);
        } catch (MalformedJwtException ex) {
            // invalid token
            throw new JwtException(JwtExceptionStatus.INVALID_TOKEN);
        } catch (ExpiredJwtException ex) {
            // expired token
            throw new JwtException(JwtExceptionStatus.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException ex) {
            // unsupported token
            throw new JwtException(JwtExceptionStatus.UNSUPPORTED_TOKEN);
        }
    }

    /**
     * method to get name for JWT token
     * @param token     JWT token from it will be username parsed
     * @return          username    - if token is valid
     *                  null        - if token is in valid
     */
    public String getNameFromToken(String token) {
        String name;
        try {
             name = parserJWTToken(token,JWT_SECRET).getBody().getSubject();
        } catch (JwtException e) {
            return null;
        }
        return name;
    }

    /**
     * TODO vymyslet neco
     * TODO prozatim odebrat z klientskeho local storage
     * Method makes token invalid
     * @param token JWT token of user who wants to be logged out
     * @return      true    - if token was successfully invalidated
     *              false   - if token in invalid or non existant
     */
    public boolean invalidateToken(String token){
        return true;
    }


}
