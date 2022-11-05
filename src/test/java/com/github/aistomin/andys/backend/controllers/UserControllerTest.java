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
import java.util.List;
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
     * Test REST template.
     */
    @Autowired
    private TestRestTemplate template;

    /**
     * Check that we can correctly perform CRUD operations with users.
     */
    @Test
    public void testCrud() {
        final UserDto user = new UserDto();
        final Long id = 1L;
        user.setId(id);
        final String username = "andrej";
        user.setUsername(username);
        final ResponseEntity<UserDto> saved = this.template.postForEntity(
            "/users", new HttpEntity<>(user), UserDto.class
        );
        Assertions.assertEquals(201, saved.getStatusCodeValue());
        Assertions.assertEquals(id, saved.getBody().getId());
        Assertions.assertEquals(username, saved.getBody().getUsername());
        final ResponseEntity<Users> selected = this.template.getForEntity(
            "/users", Users.class
        );
        Assertions.assertEquals(200, selected.getStatusCodeValue());
        final List<UserDto> users = selected.getBody().getContent();
        Assertions.assertEquals(1, users.size());
        Assertions.assertTrue(users.contains(saved.getBody()));
        final String newUsername = "new_username";
        user.setUsername(newUsername);
        final ResponseEntity<UserDto> changed = this.template.exchange(
            "/users", HttpMethod.PUT, new HttpEntity<>(user), UserDto.class
        );
        Assertions.assertEquals(200, changed.getStatusCodeValue());
        Assertions.assertEquals(id, changed.getBody().getId());
        Assertions.assertEquals(newUsername, changed.getBody().getUsername());
        final ResponseEntity<Users> updated = this.template.getForEntity(
            "/users", Users.class
        );
        Assertions.assertEquals(200, updated.getStatusCodeValue());
        Assertions.assertEquals(1, updated.getBody().getContent().size());
        Assertions.assertTrue(
            updated.getBody().getContent().contains(changed.getBody())
        );
        template.exchange(
            "/users/666",
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            Void.class
        );
        template.exchange(
            String.format("/users/%d", user.getId()),
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            Void.class
        );
        final ResponseEntity<Users> empty = this.template.getForEntity(
            "/users", Users.class
        );
        Assertions.assertEquals(200, empty.getStatusCodeValue());
        Assertions.assertEquals(0, empty.getBody().getContent().size());
    }
}
