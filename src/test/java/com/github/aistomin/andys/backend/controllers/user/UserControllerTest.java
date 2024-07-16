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
package com.github.aistomin.andys.backend.controllers.user;

import com.github.aistomin.andys.backend.controllers.Authenticator;
import com.github.aistomin.andys.backend.security.JwtRequest;
import com.github.aistomin.andys.backend.security.JwtResponse;
import com.github.aistomin.andys.backend.utils.AndysIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Test for {@link UserController}.
 *
 * @since 0.1
 */
public final class UserControllerTest extends AndysIntegrationTest {

    /**
     * Test authenticator.
     */
    @Autowired
    private Authenticator authenticator;

    /**
     * Test REST template.
     */
    @Autowired
    private TestRestTemplate template;

    /**
     * Check that we can correctly register a user.
     */
    @Test
    public void testCreateUser() {
        final int before = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Users.class
        ).getBody().getContent().size();
        final var registration = new RegistrationDto();
        registration.setUsername(UUID.randomUUID().toString());
        registration.setPassword(UUID.randomUUID().toString());
        final var unauthorised = this.template.postForEntity(
            "/users/register", new HttpEntity<>(registration), UserDto.class
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED,
            unauthorised.getStatusCode()
        );
        final ResponseEntity<UserDto> created = this.template.postForEntity(
            "/users/register",
            new HttpEntity<>(
                registration, this.authenticator.authenticateAsAdmin()
            ),
            UserDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        final List<UserDto> all = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Users.class
        ).getBody().getContent();
        Assertions.assertEquals(before + 1, all.size());
        final Optional<UserDto> found = all.stream()
            .filter(user ->
                user.getUsername().equals(registration.getUsername())
            )
            .findAny();
        Assertions.assertTrue(found.isPresent());
        final ResponseEntity<JwtResponse> auth = this.template.postForEntity(
            "/authenticate",
            new HttpEntity<>(
                new JwtRequest(
                    registration.getUsername(), registration.getPassword()
                )
            ),
            JwtResponse.class
        );
        Assertions.assertEquals(HttpStatus.OK, auth.getStatusCode());
        Assertions.assertNotNull(auth.getBody().getToken());
        final var wrongPassword = this.template.postForEntity(
            "/authenticate",
            new HttpEntity<>(
                new JwtRequest(
                    registration.getUsername(), "wrong_password"
                )
            ),
            JwtResponse.class
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED, wrongPassword.getStatusCode()
        );
    }

    /**
     * Check that we can correctly create a user.
     */
    @Test
    public void testDeleteUser() {
        final int before = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Users.class
        ).getBody().getContent().size();
        final var registration = new RegistrationDto();
        registration.setUsername(UUID.randomUUID().toString());
        registration.setPassword(UUID.randomUUID().toString());
        final ResponseEntity<UserDto> created = this.template.postForEntity(
            "/users/register",
            new HttpEntity<>(
                registration, this.authenticator.authenticateAsAdmin()
            ),
            UserDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        final UserDto found = this.template.exchange(
                "/users", HttpMethod.GET,
                new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
                Users.class
            ).getBody()
            .getContent()
            .stream()
            .filter(user ->
                user.getUsername().equals(registration.getUsername())
            )
            .findAny()
            .get();
        final ResponseEntity<Void> unauthorised = template.exchange(
            String.format("/users/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(new MultiValueMapAdapter<>(new HashMap<>())),
            Void.class
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED, unauthorised.getStatusCode()
        );
        final ResponseEntity<Void> deleted = template.exchange(
            String.format("/users/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.OK, deleted.getStatusCode());
        final ResponseEntity<Void> notFound = template.exchange(
            String.format("/users/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
        final List<UserDto> after = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Users.class
        ).getBody().getContent();
        Assertions.assertEquals(before, after.size());
        Assertions.assertTrue(
            after.stream()
                .filter(user -> user.getId().equals(found.getId()))
                .findAny()
                .isEmpty()
        );
    }

    /**
     * Check that authentication works as expected.
     */
    @Test
    void testAuthentication() {
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED,
            this.authenticator.authenticate(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
            ).getStatusCode()
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED,
            this.authenticator.authenticate(
                "admin",
                UUID.randomUUID().toString()
            ).getStatusCode()
        );
        final var forbidden = this.template.getForEntity(
            "/users", Users.class
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED, forbidden.getStatusCode()
        );
        final var allowed = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Users.class
        );
        Assertions.assertEquals(HttpStatus.OK, allowed.getStatusCode());
    }
}
