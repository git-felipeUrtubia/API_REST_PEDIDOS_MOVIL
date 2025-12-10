package com.empresa.api_level_up_movil.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    // ⚠ Poner algo más largo y privado en un proyecto real
    private static final String SECRET_KEY = "123";

    // 4 horas de duración
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 4;

    public String generateToken(String email) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    public String validateTokenAndGetEmail(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        DecodedJWT decoded = JWT.require(algorithm)
                .build()
                .verify(token);

        return decoded.getSubject(); // devuelve el email
    }

}
