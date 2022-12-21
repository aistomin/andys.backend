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
package com.github.aistomin.andys.backend.controllers;

import com.github.aistomin.andys.backend.security.JwtRequest;
import com.github.aistomin.andys.backend.security.JwtResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Test for {@link com.github.aistomin.andys.backend.controllers.user.UserController}.
 *
 * @since 0.1
 */
@Component
public final class Authenticator {

    /**
     * Test REST template.
     */
    @Autowired
    private TestRestTemplate template;

    /**
     * Authenticate as admin.
     *
     * @return Headers with JWT token.
     */
    public HttpHeaders authenticateAsAdmin() {
        final var admin = "admin";
        final var auth = this.authenticate(admin, admin);
        Assertions.assertEquals(200, auth.getStatusCode().value());
        return new HttpHeaders() {{
            set(
                "Authorization",
                String.format("Bearer %s", auth.getBody().getToken())
            );
        }};
    }

    /**
     * Authenticate user.
     *
     * @param username Username.
     * @param password Password.
     * @return Authentication response.
     */
    ResponseEntity<JwtResponse> authenticate(
        final String username, final String password
    ) {
        return this.template.postForEntity(
            "/authenticate",
            new HttpEntity<>(new JwtRequest(username, password)),
            JwtResponse.class
        );
    }
}
