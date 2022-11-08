/*
 * Copyright (c) 2021-2022, Istomin Andrei
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security config.
 *
 * @since 0.1
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * JWT authentication entry point.
     */
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * JWT user details service.
     */
    @Autowired
    private UserDetailsService service;

    /**
     * JWT request filter.
     */
    @Autowired
    private JwtRequestFilter filter;

    /**
     * Configure AuthenticationManager so that it knows from where to load user
     * for matching credentials. Use BCryptPasswordEncoder.
     *
     * @param auth Authentication manager builder.
     * @throws Exception If something goes wrong.
     */
    @Autowired
    public void configureGlobal(
        final AuthenticationManagerBuilder auth
    ) throws Exception {
        auth
            .userDetailsService(this.service)
            .passwordEncoder(passwordEncoder());
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
     * Override this method to expose the AuthenticationManager from
     * configure(AuthenticationManagerBuilder) to be exposed as a Bean.
     *
     * @return Authentication manager.
     * @throws Exception If something goes wrong.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected final void configure(final HttpSecurity http) throws Exception {
        // We don't need CSRF for this example
        http.csrf().disable()
            // dont authenticate this particular request
            .authorizeRequests()
            .antMatchers("/authenticate")
            .permitAll()
            // all other requests need to be authenticated
            .anyRequest().authenticated().and()
            // make sure we use stateless session; session won't be used to
            // store user's state.
            .exceptionHandling()
            .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Add a filter to validate the tokens with every request
        http.addFilterBefore(
            this.filter, UsernamePasswordAuthenticationFilter.class
        );
    }
}
