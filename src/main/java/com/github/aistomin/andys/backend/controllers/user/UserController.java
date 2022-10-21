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
package com.github.aistomin.andys.backend.controllers.user;

import com.github.aistomin.andys.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller.
 *
 * @since 0.1
 */
@RestController
@RequestMapping("/users")
public final class UserController {

    /**
     * User service.
     */
    private final UserService users;

    /**
     * Ctor.
     *
     * @param service User service.
     */
    public UserController(final UserService service) {
        this.users = service;
    }

    /**
     * Create a user.
     *
     * @param user User that needs to be created.
     * @return Created user.
     */
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody final UserDto user) {
        return new ResponseEntity<>(
            this.users.create(user), HttpStatus.CREATED
        );
    }

    /**
     * Update user.
     *
     * @param user User that needs to be updated.
     * @return Updated user.
     */
    @PutMapping()
    public ResponseEntity<UserDto> update(
        @RequestBody final UserDto user
    ) {
        return new ResponseEntity<>(
            this.users.update(user), HttpStatus.OK
        );
    }

    /**
     * Delete user.
     *
     * @param id User ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        this.users.delete(id);
    }

    /**
     * Load all users.
     *
     * @return Users.
     */
    @GetMapping
    public Users all() {
        return this.users.loadAll();
    }
}
