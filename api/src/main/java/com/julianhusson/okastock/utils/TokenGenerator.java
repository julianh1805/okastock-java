package com.julianhusson.okastock.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;

import java.util.Date;
import java.util.List;

@UtilityClass
public class TokenGenerator {
    public String accessToken(String email, List<String> authorities, String issuer){
        Algorithm algorithm = Algorithm.HMAC256("secret");
        return JWT.create()
                .withIssuer(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withSubject("okastock@auth.com")
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withAudience("https://okastock.julian-husson.com/auth")
                .withClaim("roles", authorities)
                .sign(algorithm);

    }

    public String refreshToken(String email, String issuer){
        Algorithm algorithm = Algorithm.HMAC256("secret");
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .sign(algorithm);

    }
}
