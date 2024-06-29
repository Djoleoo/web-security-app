package BSEP.KT2.security.jwt.implementation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import BSEP.KT2.model.User;
import BSEP.KT2.model.enums.Role;
import BSEP.KT2.security.jwt.IJwtHandler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHandler implements IJwtHandler {

    @Value("${application.jwt.secret-key}") private String SECRET_KEY; 
    @Value("${application.jwt.expiration-minutes}") private Integer accessTokenExpirationMinutes = 20;
    @Value("${application.jwt.refresh-token-expiration-days}") private Integer refreshTokenExpirationDays = 1;
    private Integer accessTokenExpirationMilliseconds = accessTokenExpirationMinutes * 60 * 1000;
    private Integer refreshTokenExpirationMilliseconds = refreshTokenExpirationDays * 24 * 60 * 60 * 1000;
	@Value("Authorization") private String AUTH_HEADER;

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole().toString());
        if(user.getRole().equals(Role.CLIENT)) {
            claims.put("package", user.getClientPackage().toString());
            claims.put("type", user.getType().toString());
        }
        return generateAccessToken(claims, user);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        var currentTimeMilliseconds = System.currentTimeMillis();
        return Jwts
            .builder()
            .claims(extraClaims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(currentTimeMilliseconds))
            .expiration(new Date(currentTimeMilliseconds + accessTokenExpirationMilliseconds))
            .signWith(getSignInKey())
            .compact();
    }

    public String generateRefreshToken(User user) {
        var currentTimeMilliseconds = System.currentTimeMillis();
        return Jwts
            .builder()
            .subject(user.getUsername())
            .claim("id", user.getId())
            .issuedAt(new Date())
            .expiration(new Date(currentTimeMilliseconds + refreshTokenExpirationMilliseconds))
            .signWith(getSignInKey())
            .compact();
    }

    public Boolean isJwtValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwt);
    }

    private Boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        var key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }

    public String getLoggedInUserUsername() {
        return extractUsername(getJwtFromContext());
    }

    public Integer extractId(String jwt) {
        return (Integer) extractAllClaims(jwt).get("id");
    }

    public Integer getLoggedInUserId() {
        return extractId(getJwtFromContext());
    }

    private String getJwtFromContext() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    //jos se ne koristi
    public Boolean canRefreshToken(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername())) && isTokenExpired(jwt);
    }
    
}

