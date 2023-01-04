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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    @Autowired
    private JwtAuthenticationEntryPoint auth;

    /**
     * JWT request filter.
     */
    @Autowired
    private JwtRequestFilter filter;

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
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(user)
            .passwordEncoder(encoder)
            .and()
            .build();
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
        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/authenticate")
            .permitAll()
            .requestMatchers(
                HttpMethod.GET,
                "/videos",
                "/music/sheets",
                "/blog/posts"
            )
            .permitAll()
            .anyRequest().authenticated().and()
            .exceptionHandling()
            .authenticationEntryPoint(this.auth)
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(
            this.filter, UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }
}
