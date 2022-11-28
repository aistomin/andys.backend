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

import com.github.aistomin.andys.backend.model.UserRepository;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User details service implementation for JWT.
 *
 * @since 0.1
 */
@Service
public final class JwtUserDetailsService implements UserDetailsService {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * User repository.
     */
    private final UserRepository repo;

    /**
     * Ctor.
     *
     * @param repository User repository.
     */
    public JwtUserDetailsService(final UserRepository repository) {
        this.repo = repository;
    }

    @Override
    public UserDetails loadUserByUsername(
        final String username
    ) throws UsernameNotFoundException {
        final var user = this.repo.findByUsername(username);
        if (user != null) {
            return new User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
            );
        } else {
            final String msg = String.format(
                "User not found with username: %s", username
            );
            logger.error(msg);
            throw new UsernameNotFoundException(msg);
        }
    }
}
