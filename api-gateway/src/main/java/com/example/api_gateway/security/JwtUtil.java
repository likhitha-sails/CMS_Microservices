package com.example.api_gateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret; //  injects from application.properties

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }

    //parses and verifies jwt sign using signing key
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //converts secret key into cryptokey for signing,required to verfiy the signature
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
