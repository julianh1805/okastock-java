package com.julianhusson.okastock.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenGenerator {

    @Value("${jwt.secret}")
    private String secret;

    public Map<String, String> generateTokens(String id, List<String> authorities, String issuer){
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken(id, authorities, issuer));
        tokens.put("refreshToken", refreshToken(id, issuer));
        return tokens;
    }

    public String accessToken(String id, List<String> authorities, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withClaim("roles", authorities)
                .sign(algorithm);

    }

    public String refreshToken(String id, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 60 * 1000))
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public DecodedJWT decodeToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
