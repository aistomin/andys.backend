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

import com.github.aistomin.andys.backend.controllers.lyrics.LyricsDto;
import com.github.aistomin.andys.backend.utils.MagicNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Test for {@link Lyrics}.
 *
 * @since 0.2
 */
final class LyricsTest {

    /**
     * Randomizer.
     */
    private final Random random = new Random();

    /**
     * Check that we correctly convert {@link LyricsDto} to {@link Lyrics}.
     */
    @Test
    void testConvert() {
        final var dto = new LyricsDto(
            this.random.nextLong(MagicNumber.THOUSAND),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var lyrics = new Lyrics(dto);
        Assertions.assertEquals(dto.getId(), lyrics.getId());
        Assertions.assertEquals(dto.getTitle(), lyrics.getTitle());
        Assertions.assertEquals(dto.getText(), lyrics.getText());
        Assertions.assertEquals(dto.getCreatedOn(), lyrics.getCreatedOn());
        Assertions.assertEquals(dto.getPublishedOn(), lyrics.getPublishedOn());
    }

    /**
     * Check that we can properly convert an object to its String
     * representation.
     */
    @Test
    void testToString() {
        final var lyrics = new Lyrics(
            this.random.nextLong(MagicNumber.THOUSAND),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var str = lyrics.toString();
        Assertions.assertTrue(
            str.contains(String.format("id=%d", lyrics.getId()))
        );
        Assertions.assertTrue(
            str.contains(String.format("title=%s", lyrics.getTitle()))
        );
    }
}
