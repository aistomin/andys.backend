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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Date;
import java.util.UUID;

/**
 * Test for {@link MusicSheetRepository}.
 *
 * @since 0.2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
final class MusicSheetRepositoryTest {

    /**
     * Music sheets repository.
     */
    @Autowired
    private MusicSheetRepository sheets;

    /**
     * Check that we can correctly save the music sheet.
     */
    @Test
    void testSaveMusicSheet() {
        final var before = this.sheets.findAll().size();
        final var sheet = new MusicSheet(
            null,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var saved = this.sheets.save(sheet);
        Assertions.assertEquals(before + 1, this.sheets.findAll().size());
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(sheet.getTitle(), saved.getTitle());
        Assertions.assertEquals(sheet.getDescription(), saved.getDescription());
        Assertions.assertEquals(sheet.getPreviewUrl(), saved.getPreviewUrl());
        Assertions.assertEquals(sheet.getDownloadUrl(), saved.getDownloadUrl());
        Assertions.assertEquals(sheet.getCreatedOn(), saved.getCreatedOn());
        Assertions.assertEquals(sheet.getPublishedOn(), saved.getPublishedOn());
    }

    /**
     * Check that we do not allow to save music sheet without "createdOn" value.
     */
    @Test
    void testCreatedOn() {
        final var sheet = new MusicSheet(
            null,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            null,
            new Date()
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.sheets.save(sheet)
        );
        sheet.setCreatedOn(new Date());
        this.sheets.save(sheet);
    }
}
