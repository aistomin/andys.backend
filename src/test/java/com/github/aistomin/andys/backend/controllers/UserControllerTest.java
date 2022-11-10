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

import com.github.aistomin.andys.backend.controllers.user.UserDto;
import com.github.aistomin.andys.backend.controllers.user.Users;
import com.github.aistomin.andys.backend.security.JwtRequest;
import com.github.aistomin.andys.backend.security.JwtResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
     * Test REST template.
     */
    @Autowired
    private TestRestTemplate template;

    /**
     * Check that we can correctly perform CRUD operations with users.
     */
    @Test
    public void testCrud() {
        final var authentication = this.authenticateAsAdmin();
        final UserDto user = new UserDto();
        final Long id = 1L;
        user.setId(id);
        final String username = "andrej";
        user.setUsername(username);
        final ResponseEntity<UserDto> saved = this.template.postForEntity(
            "/users", new HttpEntity<>(user, authentication), UserDto.class
        );
        Assertions.assertEquals(201, saved.getStatusCodeValue());
        Assertions.assertEquals(id, saved.getBody().getId());
        Assertions.assertEquals(username, saved.getBody().getUsername());
        final ResponseEntity<Users> selected = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(authentication),
            Users.class
        );
        Assertions.assertEquals(200, selected.getStatusCodeValue());
        final List<UserDto> users = selected.getBody().getContent();
        Assertions.assertEquals(1, users.size());
        Assertions.assertTrue(users.contains(saved.getBody()));
        final String newUsername = "new_username";
        user.setUsername(newUsername);
        final ResponseEntity<UserDto> changed = this.template.exchange(
            "/users", HttpMethod.PUT,
            new HttpEntity<>(user, authentication), UserDto.class
        );
        Assertions.assertEquals(200, changed.getStatusCodeValue());
        Assertions.assertEquals(id, changed.getBody().getId());
        Assertions.assertEquals(newUsername, changed.getBody().getUsername());
        final ResponseEntity<Users> updated = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(authentication),
            Users.class
        );
        Assertions.assertEquals(200, updated.getStatusCodeValue());
        Assertions.assertEquals(1, updated.getBody().getContent().size());
        Assertions.assertTrue(
            updated.getBody().getContent().contains(changed.getBody())
        );
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
        Assertions.assertEquals(200, empty.getStatusCodeValue());
        Assertions.assertEquals(0, empty.getBody().getContent().size());
    }

    /**
     * Check that authentication works as expected.
     */
    @Test
    void testAuthentication() {
        final var forbidden = this.template.getForEntity(
            "/users", Users.class
        );
        Assertions.assertEquals(401, forbidden.getStatusCodeValue());
        final var allowed = this.template.exchange(
            "/users", HttpMethod.GET,
            new HttpEntity<>(this.authenticateAsAdmin()),
            Users.class
        );
        Assertions.assertEquals(200, allowed.getStatusCodeValue());
    }

    /**
     * Authenticate as admin.
     *
     * @return Headers with JWT token.
     */
    private HttpHeaders authenticateAsAdmin() {
        final ResponseEntity<JwtResponse> auth = this.template.postForEntity(
            "/authenticate",
            new HttpEntity<>(new JwtRequest("javainuse", "password")),
            JwtResponse.class
        );
        Assertions.assertEquals(200, auth.getStatusCodeValue());
        return new HttpHeaders() {{
            set(
                "Authorization",
                String.format("Bearer %s", auth.getBody().getToken())
            );
        }};
    }
}
