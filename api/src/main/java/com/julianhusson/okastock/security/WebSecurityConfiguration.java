package com.julianhusson.okastock.security;

import com.julianhusson.okastock.security.filter.AuthenticationFilter;
import com.julianhusson.okastock.security.filter.AuthorizationFilter;
import com.julianhusson.okastock.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenGenerator tokenGenerator;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean(), tokenGenerator);
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        http.csrf().disable();
        String userRole = "USER";
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole(userRole)
                .antMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole(userRole)
                .antMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole(userRole)
                .antMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole(userRole)
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole(userRole)
                .antMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .permitAll()
                .and()
                .httpBasic();
        http.addFilterBefore(new AuthorizationFilter(tokenGenerator), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(authenticationFilter);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
