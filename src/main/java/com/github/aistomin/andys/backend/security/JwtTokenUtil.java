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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * JWT utils.
 *
 * @since 0.1
 */
@Component
public final class JwtTokenUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    /**
     * JWT token validness period in ms.
     */
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    /**
     * JWT secret.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Retrieve username from jwt token.
     *
     * @param token JWT token.
     * @return Username.
     */
    public String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retrieve expiration date from jwt token.
     *
     * @param token JWT token.
     * @return Date.
     */
    public Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Get claim from token.
     *
     * @param token    JWT token.
     * @param resolver Claims resolver.
     * @param <T>      Claim type.
     * @return Claim.
     */
    public <T> T getClaimFromToken(
        final String token, final Function<Claims, T> resolver
    ) {
        return resolver.apply(getAllClaimsFromToken(token));
    }

    /**
     * For retrieving any information from token we will need the secret key.
     *
     * @param token JWT token.
     * @return Claims.
     */
    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * Check if the token has expired.
     *
     * @param token JWT token.
     * @return True - expired; False - still valid.
     */
    private Boolean isTokenExpired(final String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Generate token for user.
     *
     * @param details User details.
     * @return Token.
     */
    public String generateToken(final UserDetails details) {
        return doGenerateToken(new HashMap<>(), details.getUsername());
    }

    /**
     *  While creating the token:
     *  1. Define  claims of the token, like Issuer, Expiration, Subject,
     *  and the ID
     *  2. Sign the JWT using the HS512 algorithm and secret key.
     *  3. According to JWS Compact Serialization.
     *  compaction of the JWT to a URL-safe string
     * @param claims Claims.
     * @param subject Subject.
     * @return Token.
     * @todo: Let's solve Issue #55 and remove this TODO.
     */
    private String doGenerateToken(
        final Map<String, Object> claims, final String subject
    ) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(
                new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)
            )
            .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Validate token.
     *
     * @param token   JWT token.
     * @param details User details.
     * @return True - token is valid; False - token is invalid.
     */
    public Boolean validateToken(
        final String token, final UserDetails details
    ) {
        final String username = getUsernameFromToken(token);
        return username.equals(details.getUsername()) && !isTokenExpired(token);
    }
}
