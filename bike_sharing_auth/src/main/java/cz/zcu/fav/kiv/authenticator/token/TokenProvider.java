package cz.zcu.fav.kiv.authenticator.token;

import cz.zcu.fav.kiv.authenticator.dials.StatusCodes;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TokenProvider {
    List<String> generateToken(Authentication authentication, boolean refreshToken);
    StatusCodes validateToken(String token, boolean isRefreshToken);
    boolean invalidateToken(String token);
    String getNameFromToken(String token);
}
