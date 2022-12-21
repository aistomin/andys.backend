/*
 * Copyright (c) 2021-2022, Istomin Andrei
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

import com.github.aistomin.andys.backend.controllers.Authenticator;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

/**
 * Test for {@link MusicSheetController}.
 *
 * @since 0.1
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class MusicSheetControllerTest {

    /**
     * Test authenticator.
     */
    @Autowired
    private Authenticator authenticator;

    /**
     * Test REST template.
     */
    @Autowired
    private TestRestTemplate template;

    /**
     * Check that we can correctly create music sheets.
     */
    @Test
    void testCreateMusicSheet() {
        final var before = this.template
            .getForEntity("/music/sheets", MusicSheets.class)
            .getBody()
            .getContent().size();
        final var sheet = new MusicSheetDto(null, UUID.randomUUID().toString());
        final ResponseEntity<MusicSheetDto> unauthorised = this.template.postForEntity(
            "/music/sheets", new HttpEntity<>(sheet), MusicSheetDto.class
        );
        Assertions.assertEquals(401, unauthorised.getStatusCode().value());
        final ResponseEntity<MusicSheetDto> created = this.template.postForEntity(
            "/music/sheets",
            new HttpEntity<>(sheet, this.authenticator.authenticateAsAdmin()),
            MusicSheetDto.class
        );
        Assertions.assertEquals(201, created.getStatusCode().value());
        Assertions.assertNotNull(created.getBody().getId());
        Assertions.assertEquals(sheet.getTitle(), created.getBody().getTitle());
        final var after = this.template
            .getForEntity("/music/sheets", MusicSheets.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before + 1, after.size());
        Assertions.assertTrue(
            after.stream()
                .filter(item -> item.getId().equals(created.getBody().getId()))
                .findFirst()
                .isPresent()
        );
    }
}
