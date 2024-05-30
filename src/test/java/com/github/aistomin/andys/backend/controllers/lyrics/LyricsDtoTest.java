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
package com.github.aistomin.andys.backend.controllers.lyrics;

import com.github.aistomin.andys.backend.model.Lyrics;
import com.github.aistomin.andys.backend.utils.MagicNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Test for {@link LyricsDto}.
 *
 * @since 0.2
 */
final class LyricsDtoTest {

    /**
     * Check that we correctly convert {@link Lyrics} to {@link LyricsDto}.
     */
    @Test
    void testConvert() {
        final var random = new Random();
        final var lyrics = new Lyrics(
            random.nextLong(MagicNumber.THOUSAND),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var dto = new LyricsDto(lyrics);
        Assertions.assertEquals(lyrics.getId(), dto.getId());
        Assertions.assertEquals(lyrics.getTitle(), dto.getTitle());
        Assertions.assertEquals(lyrics.getText(), dto.getText());
        Assertions.assertEquals(lyrics.getCreatedOn(), dto.getCreatedOn());
        Assertions.assertEquals(lyrics.getPublishedOn(), dto.getPublishedOn());
    }
}
