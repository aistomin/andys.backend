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
import com.github.aistomin.andys.backend.controllers.video.VideoDto;
import com.github.aistomin.andys.backend.utils.MagicNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

/**
 * Test for {@link Lyrics}.
 *
 * @since 0.2
 */
final class LyricsTest {

    /**
     * Check that we correctly convert {@link LyricsDto} to {@link Lyrics}.
     */
    @Test
    void testConvert() {
        final var random = new Random();
        final var dto = new LyricsDto(
            random.nextLong(MagicNumber.THOUSAND),
            UUID.randomUUID().toString(),
            new VideoDto(
                random.nextLong(MagicNumber.THOUSAND),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new HashSet<>(),
                new Date(),
                new Date()
            ),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var lyrics = new Lyrics(dto);
        Assertions.assertEquals(dto.getId(), lyrics.getId());
        Assertions.assertEquals(dto.getTitle(), lyrics.getTitle());
        Assertions.assertEquals(
            dto.getVideo().getId(), lyrics.getVideo().getId()
        );
        Assertions.assertEquals(dto.getText(), lyrics.getText());
        Assertions.assertEquals(dto.getCreatedOn(), lyrics.getCreatedOn());
        Assertions.assertEquals(dto.getPublishedOn(), lyrics.getPublishedOn());
    }
}
