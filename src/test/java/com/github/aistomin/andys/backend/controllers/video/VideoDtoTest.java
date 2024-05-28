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
package com.github.aistomin.andys.backend.controllers.video;

import com.github.aistomin.andys.backend.model.MusicSheet;
import com.github.aistomin.andys.backend.model.Video;
import com.github.aistomin.andys.backend.utils.MagicNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

/**
 * Test for {@link VideoDto}.
 *
 * @since 0.1
 */
final class VideoDtoTest {

    /**
     * Check that we correctly convert {@link Video} to {@link VideoDto}.
     */
    @Test
    void testConvert() {
        final var random = new Random();
        final var sheets = new HashSet<MusicSheet>();
        for (int i = 0; i < random.nextInt(MagicNumber.TEN); i++) {
            sheets.add(
                new MusicSheet(
                    random.nextLong(MagicNumber.THOUSAND),
                    UUID.randomUUID().toString(),
                    null,
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    new Date(),
                    new Date()
                )
            );
        }
        final var video = new Video(
            random.nextLong(MagicNumber.THOUSAND),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            sheets,
            new Date(),
            new Date()
        );
        final var dto = new VideoDto(video);
        Assertions.assertEquals(video.getId(), dto.getId());
        Assertions.assertEquals(video.getTitle(), dto.getTitle());
        Assertions.assertEquals(video.getDescription(), dto.getDescription());
        Assertions.assertEquals(video.getUrl(), dto.getUrl());
        Assertions.assertEquals(video.getYoutubeId(), dto.getYoutubeId());
        Assertions.assertEquals(
            video.getSheets().size(), dto.getSheets().size()
        );
        Assertions.assertEquals(video.getCreatedOn(), dto.getCreatedOn());
        Assertions.assertEquals(video.getPublishedOn(), dto.getPublishedOn());
    }
}
