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
import com.github.aistomin.andys.backend.data.User;
import com.github.aistomin.andys.backend.data.UserRepository;
import com.github.aistomin.andys.backend.services.UserService;
import java.util.Objects;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * User's service's implementation.
 *
 * @since 0.1
 */
@Service
public final class UserServiceImpl implements UserService {

    /**
     * User repository.
     */
    private final UserRepository repo;

    /**
     * Password encoder.
     */
    private final PasswordEncoder enc;

    /**
     * Ctor.
     *
     * @param repository User repository.
     * @param encoder Password encoder.
     */
    public UserServiceImpl(
        final UserRepository repository, final PasswordEncoder encoder
    ) {
        this.repo = repository;
        this.enc = encoder;
    }

    /**
     * Create a user.
     *
     * @param user User that needs to be created.
     * @return Created user.
     */
    @Override
    public UserDto create(final UserDto user) {
        return save(user);
    }

    @Override
    public UserDto update(final UserDto user) {
        final Long id = user.getId();
        final UserDto existing = this.loadAll()
            .getContent()
            .stream()
            .filter(item -> Objects.equals(item.getId(), id))
            .findAny()
            .orElse(null);
        if (null == existing) {
            throw new IllegalStateException(
                String.format("User with ID = %d not found.", id)
            );
        }
        existing.setUsername(user.getUsername());
        return this.save(existing);
    }

    @Override
    public void delete(final Long id) {
        final UserDto user = this.loadAll()
            .getContent()
            .stream()
            .filter(item -> Objects.equals(item.getId(), id))
            .findAny()
            .orElse(null);
        if (null == user) {
            throw new IllegalStateException(
                String.format("User with ID = %d not found.", id)
            );
        }
        this.repo.delete(new User(user));
    }

    /**
     * Load all users.
     *
     * @return Users
     */
    @Override
    public Users loadAll() {
        final Users users = new Users();
        users.setContent(
            this.repo.findAll().stream().map(UserDto::new).toList()
        );
        return users;
    }

    /**
     * Save user.
     *
     * @param user User to save.
     * @return Saved user.
     */
    private UserDto save(final UserDto user) {
        if (user.getPassword() != null) {
            user.setPassword(this.enc.encode(user.getPassword()));
        } else {
            user.setPassword(
                this.repo.findByUsername(user.getUsername()).getPassword()
            );
        }
        return new UserDto(this.repo.save(new User(user)));
    }
}
