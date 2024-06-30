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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JWT request filter.
 *
 * @since 0.1
 */
@Component
public final class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * User details service.
     */
    private final UserDetailsService service;

    /**
     * JWT utils.
     */
    private final Jwt jwt;

    /**
     * Ctor.
     *
     * @param userDetails User details service.
     * @param utils JWT utils.
     */
    public JwtRequestFilter(
        final UserDetailsService userDetails,
        final Jwt utils
    ) {
        this.service = userDetails;
        this.jwt = utils;
    }

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain chain
    ) throws ServletException, IOException {
        final var authorization = request.getHeader("Authorization");
        final var bearer = "Bearer ";
        if (authorization != null && authorization.startsWith(bearer)) {
            final var jwtToken = authorization.substring(bearer.length());
            final var username = this.jwt.getUsernameFromToken(jwtToken);
            final var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
            if (auth == null) {
                UserDetails userDetails = this.service
                    .loadUserByUsername(username);
                if (this.jwt.validateToken(jwtToken, userDetails)) {
                    final var token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                    token.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
        } else {
            this.logger.debug("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request, response);
    }
}
