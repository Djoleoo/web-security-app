package BSEP.KT2.security.jwt;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import BSEP.KT2.model.User;

import io.jsonwebtoken.Claims;

public interface IJwtHandler {

    String extractUsername(String jwt);
    
    Integer extractId(String jwt);

    <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver);

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String generateAccessToken(UserDetails userDetails);

    String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails);
    
    Boolean isJwtValid(String jwt, UserDetails userDetails);

    String getLoggedInUserUsername();

    Integer getLoggedInUserId();

    Boolean canRefreshToken(String jwt,UserDetails userDetails);
    
}