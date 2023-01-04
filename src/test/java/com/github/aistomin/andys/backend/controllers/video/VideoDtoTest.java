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

import com.github.aistomin.andys.backend.model.Video;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        final var video = new Video(
            new Random().nextLong(1000),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date(),
            Arrays.asList("video", "testing", "conversion")
        );
        final var dto = new VideoDto(video);
        Assertions.assertEquals(video.getId(), dto.getId());
        Assertions.assertEquals(video.getTitle(), dto.getTitle());
        Assertions.assertEquals(video.getDescription(), dto.getDescription());
        Assertions.assertEquals(video.getUrl(), dto.getUrl());
        Assertions.assertEquals(video.getCreatedOn(), dto.getCreatedOn());
        Assertions.assertEquals(video.getPublishedOn(), dto.getPublishedOn());
        Assertions.assertEquals(video.getTags().size(), dto.getTags().size());
        Assertions.assertTrue(
            video.getTags().stream()
                .filter(tag -> !dto.getTags().contains(tag))
                .findAny()
                .isEmpty()
        );
    }
}
