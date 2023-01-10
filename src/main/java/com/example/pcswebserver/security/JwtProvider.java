package com.example.pcswebserver.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    public String generateToken(String username) {
        return JWT.create()
                .withIssuer(jwtIssuer)
                .withSubject(username)
                .withExpiresAt(LocalDate.now()
                        .plusDays(15)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant())
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public Optional<DecodedJWT> toDecodedJWT(String token) {
        try {
            return Optional.of(JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(jwtIssuer)
                    .build()
                    .verify(token));
        } catch (JWTVerificationException exception) {
            log.warn("Invalid protocol for token {}", token);
            return Optional.empty();
        }
    }

    public String getUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(jwtSecret))
                .withIssuer(jwtIssuer)
                .build()
                .verify(token)
                .getSubject();
    }


}