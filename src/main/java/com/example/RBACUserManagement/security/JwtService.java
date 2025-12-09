package com.example.RBACUserManagement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final SecretKey key;
    private final long jwtExpirationMs;

    public JwtService(@Value("${app.jwt.secret:change_me_to_secure_random_string}") String secret,
                      @Value("${app.jwt.expiration-ms:86400000}") long jwtExpirationMs) {
        // secret must be sufficiently long (>=32 bytes) for HS256. In production provide via env/secret manager.
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(String subject, Set<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject)
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(jwtExpirationMs)))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getSubject(String token) {
        return parseToken(token).getBody().getSubject();
    }

    @SuppressWarnings("unchecked")
    public Set<String> getRoles(String token) {
        Object rolesObj = parseToken(token).getBody().get("roles");
        if (rolesObj instanceof Collection) {
            return ((Collection<?>) rolesObj).stream().map(Object::toString).collect(Collectors.toSet());
        }
        return Set.of();
    }
}

