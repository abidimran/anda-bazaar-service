package com.andabazaar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.andabazaar.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // =====================================================
    // AUTHENTICATION MANAGER
    // =====================================================

    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http) throws Exception {

        http

            // =================================================
            // CSRF
            // =================================================

            .csrf(AbstractHttpConfigurer::disable
            )

            // =================================================
            // SESSION
            // =================================================

            .sessionManagement(session ->
                session.sessionCreationPolicy( SessionCreationPolicy.STATELESS )
            )

            // =================================================
            // AUTHORIZATION
            // =================================================

            .authorizeHttpRequests(auth -> auth

                // =================================================
                // PUBLIC ENDPOINTS
                // =================================================

                .requestMatchers( "/api/auth/register", "/api/auth/login", "/actuator/health" ).permitAll()

                // =================================================
                // EGG PRICE - ADMIN WRITE OPERATIONS
                // =================================================
                // Only ADMIN can:
                // POST   /api/egg-prices
                // PUT    /api/egg-prices/{id}
                // DELETE /api/egg-prices/{id}
                // =================================================

                .requestMatchers( org.springframework.http.HttpMethod.POST, "/api/egg-prices" ).hasRole("ADMIN")

                .requestMatchers( org.springframework.http.HttpMethod.PUT, "/api/egg-prices/**" ).hasRole("ADMIN")

                .requestMatchers( org.springframework.http.HttpMethod.DELETE, "/api/egg-prices/**" ).hasRole("ADMIN")

                // =================================================
                // EGG PRICE - READ OPERATIONS
                // =================================================
                // Logged-in users can read prices.
                // Subscription/payment logic should be handled
                // inside service/security business logic.
                // =================================================

                .requestMatchers( org.springframework.http.HttpMethod.GET, "/api/egg-prices/**" ).authenticated()

                // =================================================
                // EXTERNAL EGG RATES API
                // =================================================

                .requestMatchers( "/api/egg-rates-external/**" ).authenticated()

                // =================================================
                // AUTH
                // =================================================

                .requestMatchers( "/api/auth/me" ).authenticated()

                // =================================================
                // USERS
                // =================================================

                .requestMatchers( "/api/users/**" ).authenticated()

                // =================================================
                // STATES
                // =================================================

                .requestMatchers( "/api/states/**" ).authenticated()

                // =================================================
                // EVERYTHING ELSE
                // =================================================

                .anyRequest().authenticated()
            )

            // =================================================
            // JWT FILTER
            // =================================================

            .addFilterBefore( jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}