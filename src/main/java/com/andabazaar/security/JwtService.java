package com.andabazaar.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.andabazaar.config.JwtConfig;
import com.andabazaar.repository.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtConfig.getSecret().getBytes());
    }

    public String generateToken(User user) {

        Date now = new Date();

        Date expiration = new Date( now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {

        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid( String token, User user) {

        try {

            String email = extractEmail(token);

            return email.equals(user.getEmail())
                    && !isTokenExpired(token);

        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {

        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}