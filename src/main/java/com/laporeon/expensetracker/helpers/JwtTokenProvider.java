package com.laporeon.expensetracker.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.laporeon.expensetracker.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final int TOKEN_EXPIRATION_TIME_IN_SECONDS = 7200;
    private static final String ISSUER = "expense-tracker-api-auth";

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
        return JWT.create()
                  .withIssuer(ISSUER)
                  .withSubject(user.getId().toString())
                  .withIssuedAt(Instant.now())
                  .withExpiresAt(generateExpirationDate())
                  .sign(algorithm);
    }


    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
        return JWT.require(algorithm)
                  .withIssuer(ISSUER)
                  .build()
                  .verify(token)
                  .getSubject();

    }

    private Instant generateExpirationDate() {
        return Instant.now().plusSeconds(TOKEN_EXPIRATION_TIME_IN_SECONDS);
    }

}