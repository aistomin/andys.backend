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
package com.github.aistomin.andys.backend.model;

import com.github.aistomin.andys.backend.controllers.user.RegistrationDto;
import com.github.aistomin.andys.backend.utils.MagicNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Test for {@link User}.
 *
 * @since 0.1
 */
final class UserTest {

    /**
     * Randomizer.
     */
    private final Random random = new Random();

    /**
     * Check that we correctly convert {@link RegistrationDto} to
     * {@link com.github.aistomin.andys.backend.model.User}.
     */
    @Test
    void testConvert() {
        final var dto = new RegistrationDto(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        );
        final User user = new User(dto);
        Assertions.assertEquals(dto.getUsername(), user.getUsername());
    }

    /**
     * Check that we can properly convert an object to its String
     * representation.
     */
    @Test
    void testToString() {
        final var video = new User(
            this.random.nextLong(MagicNumber.THOUSAND),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date()
        );
        final var str = video.toString();
        Assertions.assertTrue(
            str.contains(String.format("id=%d", video.getId()))
        );
        Assertions.assertTrue(
            str.contains(String.format("username=%s", video.getUsername()))
        );
        Assertions.assertFalse(
            str.contains(String.format("password=%s", video.getUsername()))
        );
    }
}
