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

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
    @Autowired
    private UserDetailsService service;

    /**
     * JWT utils.
     */
    @Autowired
    private Jwt utils;

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain chain
    ) throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        final String bearer = "Bearer ";
        if (authorization != null && authorization.startsWith(bearer)) {
            jwtToken = authorization.substring(bearer.length());
            try {
                username = this.utils.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                this.logger.warn("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                this.logger.warn("JWT Token has expired");
            }
        } else {
            this.logger.warn("JWT Token does not begin with Bearer String");
        }
        // Once we get the token validate it.
        final var auth = SecurityContextHolder.getContext().getAuthentication();
        if (username != null && auth == null) {
            UserDetails userDetails = this.service.loadUserByUsername(username);
            // if token is valid configure Spring Security to manually set
            // authentication
            if (this.utils.validateToken(jwtToken, userDetails)) {
                final var token = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        chain.doFilter(request, response);
    }
}
