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

import com.github.aistomin.andys.backend.utils.AndysIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Date;
import java.util.UUID;

/**
 * Test for {@link LyricsRepository}.
 *
 * @since 0.2
 */
final class LyricsRepositoryTest extends AndysIntegrationTest {

    /**
     * Lyrics repository.
     */
    @Autowired
    private LyricsRepository lyrics;

    /**
     * Check that we can correctly save the lyrics.
     */
    @Test
    void testSaveLyrics() {
        final var before = this.lyrics.findAll().size();
        final var item = new Lyrics(
            null,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var saved = this.lyrics.save(item);
        Assertions.assertEquals(before + 1, this.lyrics.findAll().size());
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(item.getTitle(), saved.getTitle());
        Assertions.assertEquals(item.getText(), saved.getText());
        Assertions.assertEquals(item.getCreatedOn(), saved.getCreatedOn());
        Assertions.assertEquals(item.getPublishedOn(), saved.getPublishedOn());
    }

    /**
     * Check that we do not allow to save lyrics without "createdOn" value.
     */
    @Test
    void testCreatedOn() {
        final var item = new Lyrics(
            null,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            null,
            new Date()
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.lyrics.save(item)
        );
        item.setCreatedOn(new Date());
        this.lyrics.save(item);
    }
}
