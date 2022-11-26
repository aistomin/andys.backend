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

import com.github.aistomin.andys.backend.controllers.user.RegistrationDto;
import com.github.aistomin.andys.backend.controllers.user.UserDto;
import com.github.aistomin.andys.backend.controllers.user.Users;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Test for {@link com.github.aistomin.andys.backend.controllers.user.UserController}.
 *
 * @since 0.1
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class UserControllerTest {

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
     * Check that we can correctly perform CRUD operations with users.
     *
     * @todo: Let's fix Issue #57 and remove this TODO.
     */
    @Test
    public void testCrud() {
        final var authentication = this.authenticator.authenticateAsAdmin();
        final List<UserDto> content = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(authentication),
            Users.class
        ).getBody().getContent();
        final var before = content.size();
        final var registration = new RegistrationDto();
        final var username = UUID.randomUUID().toString();
        registration.setUsername(username);
        registration.setPassword(UUID.randomUUID().toString());
        final ResponseEntity<UserDto> saved = this.template.postForEntity(
            "/users/register", new HttpEntity<>(registration, authentication), UserDto.class
        );
        Assertions.assertEquals(201, saved.getStatusCode().value());
        final var user = saved.getBody();
        Assertions.assertEquals(username, user.getUsername());
        final ResponseEntity<Users> selected = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(authentication),
            Users.class
        );
        Assertions.assertEquals(200, selected.getStatusCode().value());
        final List<UserDto> users = selected.getBody().getContent();
        Assertions.assertEquals(before + 1, users.size());
        Assertions.assertTrue(users.contains(saved.getBody()));
        template.exchange(
            "/users/666",
            HttpMethod.DELETE,
            new HttpEntity<>(authentication),
            Void.class
        );
        template.exchange(
            String.format("/users/%d", user.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(authentication),
            Void.class
        );
        final ResponseEntity<Users> empty = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(authentication),
            Users.class
        );
        Assertions.assertEquals(200, empty.getStatusCode().value());
        Assertions.assertEquals(before, empty.getBody().getContent().size());
    }

    /**
     * Check that authentication works as expected.
     */
    @Test
    void testAuthentication() {
        Assertions.assertEquals(
            401,
            this.authenticator.authenticate(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
            ).getStatusCode().value()
        );
        Assertions.assertEquals(
            401,
            this.authenticator.authenticate(
                "admin",
                UUID.randomUUID().toString()
            ).getStatusCode().value()
        );
        final var forbidden = this.template.getForEntity(
            "/users", Users.class
        );
        Assertions.assertEquals(401, forbidden.getStatusCode().value());
        final var allowed = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Users.class
        );
        Assertions.assertEquals(200, allowed.getStatusCode().value());
    }
}
