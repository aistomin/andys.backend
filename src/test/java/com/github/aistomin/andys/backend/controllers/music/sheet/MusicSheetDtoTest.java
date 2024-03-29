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
package com.github.aistomin.andys.backend.controllers.music.sheet;

import com.github.aistomin.andys.backend.model.MusicSheet;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import com.github.aistomin.andys.backend.model.Video;
import com.github.aistomin.andys.backend.utils.MagicNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link MusicSheetDto}.
 *
 * @since 0.1
 */
final class MusicSheetDtoTest {

    /**
     * Check that we correctly convert {@link MusicSheet} to
     * {@link MusicSheetDto}.
     */
    @Test
    void testConvert() {
        final var random = new Random();
        final var sheet = new MusicSheet(
            random.nextLong(MagicNumber.THOUSAND),
            UUID.randomUUID().toString(),
            new Video(
                random.nextLong(MagicNumber.THOUSAND),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new HashSet<>(),
                new Date(),
                new Date()
            ),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var dto = new MusicSheetDto(sheet);
        Assertions.assertEquals(sheet.getId(), dto.getId());
        Assertions.assertEquals(sheet.getTitle(), dto.getTitle());
        Assertions.assertEquals(
            sheet.getVideo().getId(), dto.getVideo().getId()
        );
        Assertions.assertEquals(sheet.getDescription(), dto.getDescription());
        Assertions.assertEquals(sheet.getPreviewUrl(), dto.getPreviewUrl());
        Assertions.assertEquals(sheet.getDownloadUrl(), dto.getDownloadUrl());
        Assertions.assertEquals(sheet.getCreatedOn(), dto.getCreatedOn());
        Assertions.assertEquals(sheet.getPublishedOn(), dto.getPublishedOn());
    }
}
