package com.example.E_Commerce.security;

import com.example.E_Commerce.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.DoubleStream;

@Service
@Slf4j
public class JwtUtils {

    private static final long EXPIRATION_TIME = 1000L * 60L * 60L * 24L * 30L * 6L; //expires 6 months
    private SecretKey key;

    @Value("${jwt.secreteJwtString}")
    private String secreteJwtString; //Make sure the value in the application properties is 32characters or long

    /**
     *  After secreteJwtString is injected , the init() will be
     *  executed to set up the cryptographic key
     */
    @PostConstruct
    private void init() {
        byte[] keyBytes = secreteJwtString.getBytes(StandardCharsets.UTF_8); // Convert string to bytes
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256"); // Create the secret key for JWT signing
    }

    public String generateToken(User user) {
        String username = user.getEmail();
        return generateToken(username);
    }

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser()
                .verifyWith(key)   // Verify the token using HMAC-SHA256
                .build()
                .parseSignedClaims(token) // Parse and verify the token
                .getPayload()); // Extract claims
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }








}
