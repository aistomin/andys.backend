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
import com.github.aistomin.andys.backend.utils.MagicNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

/**
 * Test for {@link MusicSheetDto}.
 *
 * @since 0.1
 */
final class MusicSheetDtoTest {

    /**
     * Randomizer.
     */
    private final Random random = new Random();

    /**
     * Check that we correctly convert {@link MusicSheet} to
     * {@link MusicSheetDto}.
     */
    @Test
    void testConvert() {
        final var sheet = createTestSheet();
        final var dto = new MusicSheetDto(sheet);
        Assertions.assertEquals(sheet.getId(), dto.getId());
        Assertions.assertEquals(sheet.getTitle(), dto.getTitle());
        Assertions.assertEquals(sheet.getDescription(), dto.getDescription());
        Assertions.assertEquals(sheet.getPreviewUrl(), dto.getPreviewUrl());
        Assertions.assertEquals(sheet.getDownloadUrl(), dto.getDownloadUrl());
        Assertions.assertEquals(sheet.getCreatedOn(), dto.getCreatedOn());
        Assertions.assertEquals(sheet.getPublishedOn(), dto.getPublishedOn());
    }

    /**
     * Check that we can properly convert an object to its String
     * representation.
     */
    @Test
    void testToString() {
        final var sheet = new MusicSheetDto(createTestSheet());
        final var str = sheet.toString();
        Assertions.assertTrue(
            str.contains(String.format("id=%d", sheet.getId()))
        );
        Assertions.assertTrue(
            str.contains(String.format("title=%s", sheet.getTitle()))
        );
    }

    /**
     * Check that we correctly compare music sheets.
     */
    @Test
    void testEquals() {
        final var sheets = Arrays.asList(
            new MusicSheetDto(createTestSheet()),
            new MusicSheetDto(createTestSheet()),
            new MusicSheetDto(createTestSheet())
        );
        Collections.shuffle(sheets);
        final var sheet = sheets.get(0);
        final var modified = new MusicSheetDto(
            sheet.getId(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        Assertions.assertEquals(sheet, modified);
        Assertions.assertEquals(0, sheets.indexOf(modified));
    }

    /**
     * Check that we correctly compute the hash code for the music sheet.
     */
    @Test
    void testHashCode() {
        final var sheets = new HashSet<>(
            Arrays.asList(
                new MusicSheetDto(createTestSheet()),
                new MusicSheetDto(createTestSheet()),
                new MusicSheetDto(createTestSheet())
            )
        );
        Assertions.assertEquals(MagicNumber.THREE, sheets.size());
        final var sheet = sheets.iterator().next();
        final var modified = new MusicSheetDto(
            sheet.getId(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        Assertions.assertEquals(sheet.hashCode(), modified.hashCode());
        sheets.add(modified);
        Assertions.assertEquals(MagicNumber.THREE, sheets.size());
    }

    /**
     * Create a test music sheet with random values.
     *
     * @return A test music sheet.
     */
    private MusicSheet createTestSheet() {
        return new MusicSheet(
            random.nextLong(MagicNumber.THOUSAND),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
    }
}
