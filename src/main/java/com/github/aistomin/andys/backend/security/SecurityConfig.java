/*
 * Copyright (c) 2022-2023, Istomin Andrei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.aistomin.andys.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

/**
 * Security config.
 *
 * @since 0.1
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * JWT authentication entry point.
     */
    private final JwtAuthenticationEntryPoint auth;

    /**
     * JWT request filter.
     */
    private final JwtRequestFilter filter;

    /**
     * Ctor.
     *
     * @param entryPoint       JWT authentication entry point.
     * @param jwtRequestFilter JWT request filter.
     */
    public SecurityConfig(
        final JwtAuthenticationEntryPoint entryPoint,
        final JwtRequestFilter jwtRequestFilter
    ) {
        this.auth = entryPoint;
        this.filter = jwtRequestFilter;
    }

    /**
     * Create authentication manager.
     *
     * @param http    HTTP security.
     * @param encoder Password encoder.
     * @param user    User details.
     * @return Authentication manager.
     * @throws Exception If something goes wrong.
     */
    @Bean
    public AuthenticationManager authManager(
        final HttpSecurity http,
        final PasswordEncoder encoder,
        final UserDetailsService user
    ) throws Exception {
        final var builder = http.getSharedObject(
            AuthenticationManagerBuilder.class
        );
        builder
            .userDetailsService(user)
            .passwordEncoder(encoder);
        return builder.build();
    }

    /**
     * Create password encoder.
     *
     * @return Password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS configuration.
     *
     * @return CORS configuration.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET"));
        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Filter chain.
     *
     * @param http HTTP security.
     * @return Security filter chain.
     * @throws Exception If something goes wrong.
     */
    @Bean
    public SecurityFilterChain filterChain(
        final HttpSecurity http
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(configurer -> configurer.configure(http))
            .authorizeHttpRequests(registry -> {
                registry.requestMatchers("/authenticate")
                    .permitAll()
                    .requestMatchers("/contact/us")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/videos",
                        "/music/sheets",
                        "/blog/posts",
                        "/photos",
                        "/lyrics"
                    )
                    .permitAll()
                    .anyRequest().authenticated();
            })
            .exceptionHandling(configurer -> {
                configurer.configure(http);
                configurer.authenticationEntryPoint(this.auth);
            })
            .sessionManagement(configurer -> {
                configurer.configure(http);
                configurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                );
            });
        http.addFilterBefore(
            this.filter, UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }
}
