package com.julianhusson.okastock.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class TokenGeneratorTest {

    @Autowired
    private TokenGenerator underTest;

    @Test
    void itShouldGenerateTokens() {
        //Given
        String email = "test@test.com";
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_USER");
        String issuer = "test/api/v1/auth/login";
        //When
        Map<String, String> tokens = underTest.generateTokens(email, authorities, issuer);
        //Then
        assertThat(tokens.get("accessToken")).isNotEmpty();
        assertThat(tokens.get("refreshToken")).isNotEmpty();
    }

    @Test
    void itShouldGenerateAccessToken() {
        //Given
        String email = "test@test.com";
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_USER");
        String issuer = "test/api/v1/auth/login";
        Date date = new Date();
        Date expiresDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        //When
        DecodedJWT decodedToken = underTest.decodeToken(underTest.accessToken(email, authorities, issuer));
        //Then
        assertThat(decodedToken.getSubject()).isEqualTo(email);
        assertThat(decodedToken.getExpiresAt()).isCloseTo(expiresDate, 1000L);
        assertThat(decodedToken.getIssuedAt()).isCloseTo(date, 1000L);
        assertThat(decodedToken.getIssuer()).isEqualTo(issuer);
        assertThat(decodedToken.getClaim("roles").asList(String.class).get(0)).isEqualTo("ROLE_USER");
    }

    @Test
    void itShouldGenerateRefreshToken() {
        String email = "test@test.com";
        String issuer = "test/api/v1/auth/login";
        Date date = new Date();
        Date expiresDate = new Date(System.currentTimeMillis() + 3600 * 60 * 1000);
        //When
        DecodedJWT decodedToken = underTest.decodeToken(underTest.refreshToken(email, issuer));
        //Then
        assertThat(decodedToken.getSubject()).isEqualTo(email);
        assertThat(decodedToken.getExpiresAt()).isCloseTo(expiresDate, 1000L);
        assertThat(decodedToken.getIssuedAt()).isCloseTo(date, 1000L);
        assertThat(decodedToken.getIssuer()).isEqualTo(issuer);
    }
}