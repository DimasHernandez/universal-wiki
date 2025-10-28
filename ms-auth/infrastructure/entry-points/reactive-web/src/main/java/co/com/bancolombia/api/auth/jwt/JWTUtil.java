package co.com.bancolombia.api.auth.jwt;

import co.com.bancolombia.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JWTUtil {

    private static final String ROLES = "roles";

    private final String secret;

    private final String expirationRefresh;

    private final String expirationAccessToken;

    private SecretKey key;

    public JWTUtil(@Value("${jwt.secret}") String jwtSecret, @Value("${jwt.expirationRefreshToken}") String expirationRefresh,
                   @Value("${jwt.expirationAccessToken}") String expirationAccessToken) {
        this.secret = jwtSecret;
        this.expirationRefresh = expirationRefresh;
        this.expirationAccessToken = expirationAccessToken;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        try {
            log.info("Attempting to extract claims from token {}", token);
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            throw new JwtException(e.getMessage(), e);
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES, user.getRoles());
        int timeInHours = Integer.parseInt(expirationRefresh);
        return createToken(claims, user.getUsername(), timeInHours);
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES, user.getRoles());
        int timeInMinutes = Integer.parseInt(expirationAccessToken);
        return createToken(claims, user.getUsername(), timeInMinutes);
    }

    private String createToken(Map<String, Object> claims, String subject, int timeLongDays) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + timeLongDays * 1000L);
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(createdDate)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.info("Invalid JWT Token {}", e.getMessage());
            return false;
        }
    }
}
