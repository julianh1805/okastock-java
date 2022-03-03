package com.julianhusson.okastock.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julianhusson.okastock.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authorizationCredentials = request.getHeader(HttpHeaders.AUTHORIZATION).substring("Basic".length()).trim();
        String[] decodedCredentials = new String(Base64.getDecoder().decode(authorizationCredentials)).split(":");
        String username = decodedCredentials[0];
        String password = decodedCredentials[1];
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = (User)authResult.getPrincipal();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", TokenGenerator.accessToken(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(), request.getRequestURL().toString()));
        tokens.put("refreshToken", TokenGenerator.refreshToken(user.getUsername(), request.getRequestURL().toString()));
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
}
