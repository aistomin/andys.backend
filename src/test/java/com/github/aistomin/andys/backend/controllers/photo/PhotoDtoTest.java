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
package com.github.aistomin.andys.backend.controllers.photo;

import com.github.aistomin.andys.backend.model.Photo;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link PhotoDto}.
 *
 * @since 0.1
 */
final class PhotoDtoTest {

    /**
     * Check that we correctly convert {@link Photo} to {@link PhotoDto}.
     */
    @Test
    void testConvert() {
        final var photo = new Photo(
            new Random().nextLong(1000),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var dto = new PhotoDto(photo);
        Assertions.assertEquals(photo.getId(), dto.getId());
        Assertions.assertEquals(photo.getDescription(), dto.getDescription());
        Assertions.assertEquals(photo.getUrl(), dto.getUrl());
        Assertions.assertEquals(photo.getCreatedOn(), dto.getCreatedOn());
        Assertions.assertEquals(photo.getPublishedOn(), dto.getPublishedOn());
    }
}
