package com.julianhusson.okastock.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
class TokenGeneratorTest {

    @Test
    void itShouldGenerateAccessToken() {
        //Given
        String email = "test@test.com";
        List<String> authorities = new ArrayList<>();
        authorities.add(Role.USER.toString());
        String issuer = "http://test/api/v1/auth/login";
        Date date = new Date();
        Date expiresDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        //When
        DecodedJWT decodedToken = TokenGenerator.decodeToken(TokenGenerator.accessToken(email, authorities, issuer));
        //Then
        assertThat(decodedToken.getSubject()).isEqualTo(email);
        assertThat(decodedToken.getExpiresAt()).isCloseTo(expiresDate, 1000L);
        assertThat(decodedToken.getIssuedAt()).isCloseTo(date, 1000L);
        assertThat(decodedToken.getIssuer()).isEqualTo(issuer);
        assertThat(decodedToken.getClaim("roles").asList(String.class).get(0)).isEqualTo(Role.USER.toString());
    }

    @Test
    void itShouldGenerateRefreshToken() {
        String email = "test@test.com";
        String issuer = "http://test/api/v1/auth/login";
        Date date = new Date();
        Date expiresDate = new Date(System.currentTimeMillis() + 3600 * 60 * 1000);
        //When
        DecodedJWT decodedToken = TokenGenerator.decodeToken(TokenGenerator.refreshToken(email, issuer));
        //Then
        assertThat(decodedToken.getSubject()).isEqualTo(email);
        assertThat(decodedToken.getExpiresAt()).isCloseTo(expiresDate, 1000L);
        assertThat(decodedToken.getIssuedAt()).isCloseTo(date, 1000L);
        assertThat(decodedToken.getIssuer()).isEqualTo(issuer);
    }
}