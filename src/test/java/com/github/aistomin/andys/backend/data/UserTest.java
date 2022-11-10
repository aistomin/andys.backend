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
package com.github.aistomin.andys.backend.data;

import com.github.aistomin.andys.backend.controllers.user.UserDto;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link User}.
 *
 * @since 0.1
 */
final class UserTest {

    /**
     * Check that we correctly convert {@link UserDto} to
     * {@link com.github.aistomin.andys.backend.data.User}.
     */
    @Test
    void testConvert() {
        final UserDto dto = new UserDto(
            new Random().nextLong(1000),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        );
        final User user = new User(dto);
        Assertions.assertEquals(dto.getId(), user.getId());
        Assertions.assertEquals(dto.getUsername(), user.getUsername());
    }
}
