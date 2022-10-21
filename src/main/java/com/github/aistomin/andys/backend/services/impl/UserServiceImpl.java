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
package com.github.aistomin.andys.backend.services.impl;

import com.github.aistomin.andys.backend.controllers.user.UserDto;
import com.github.aistomin.andys.backend.controllers.user.Users;
import com.github.aistomin.andys.backend.services.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 * User's service's implementation.
 *
 * @since 0.1
 */
@Service
public final class UserServiceImpl implements UserService {

    /**
     * Temporary storage.
     *
     * @todo Issue #27. Replace it with the real database.
     */
    private final List<UserDto> storage = new ArrayList<>();

    /**
     * Create a user.
     *
     * @param user User that needs to be created.
     * @return Created user.
     */
    @Override
    public UserDto create(final UserDto user) {
        this.storage.add(user);
        return user;
    }

    @Override
    public UserDto update(final UserDto user) {
        final Long id = user.getId();
        final UserDto existing = this.storage
            .stream()
            .filter(item -> {
                return Objects.equals(item.getId(), id);
            })
            .findAny()
            .orElse(null);
        if (null == existing) {
            throw new IllegalStateException(
                String.format("User with ID = %d not found.", id)
            );
        }
        existing.setUsername(user.getUsername());
        return existing;
    }

    @Override
    public void delete(final Long id) {
        final UserDto user = this.storage
            .stream()
            .filter(item -> Objects.equals(item.getId(), id))
            .findAny()
            .orElse(null);
        if (null == user) {
            throw new IllegalStateException(
                String.format("User with ID = %d not found.", id)
            );
        }
        this.storage.remove(user);
    }

    /**
     * Load all users.
     *
     * @return Users
     */
    @Override
    public Users loadAll() {
        final Users users = new Users();
        users.setContent(this.storage);
        return users;
    }
}
