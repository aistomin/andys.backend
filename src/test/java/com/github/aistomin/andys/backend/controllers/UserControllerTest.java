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
        user.setId(1L);
        user.setUsername("andrej");
        final ResponseEntity<String> saved = this.template.postForEntity(
            "/users", new HttpEntity<>(user), String.class
        );
        Assertions.assertEquals(201, saved.getStatusCodeValue());
        Assertions.assertEquals(
            "{\"id\":1,\"username\":\"andrej\"}", saved.getBody()
        );
        final ResponseEntity<String> selected = this.template.getForEntity(
            "/users", String.class
        );
        Assertions.assertEquals(200, selected.getStatusCodeValue());
        Assertions.assertEquals(
            "{\"content\":[{\"id\":1,\"username\":\"andrej\"}]}",
            selected.getBody()
        );
        user.setUsername("new_username");
        final ResponseEntity<String> changed = this.template.exchange(
            "/users", HttpMethod.PUT, new HttpEntity<>(user), String.class
        );
        Assertions.assertEquals(200, changed.getStatusCodeValue());
        Assertions.assertEquals(
            "{\"id\":1,\"username\":\"new_username\"}", changed.getBody()
        );
        final ResponseEntity<String> updated = this.template.getForEntity(
            "/users", String.class
        );
        Assertions.assertEquals(200, updated.getStatusCodeValue());
        Assertions.assertEquals(
            "{\"content\":[{\"id\":1,\"username\":\"new_username\"}]}",
            updated.getBody()
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
        final ResponseEntity<String> empty = this.template.getForEntity(
            "/users", String.class
        );
        Assertions.assertEquals(200, empty.getStatusCodeValue());
        Assertions.assertEquals(
            "{\"content\":[]}",
            empty.getBody()
        );
    }
}
