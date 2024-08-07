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

import com.github.aistomin.andys.backend.controllers.Authenticator;
import com.github.aistomin.andys.backend.utils.AndysIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Test for {@link MusicSheetController}.
 *
 * @since 0.1
 */
public final class MusicSheetControllerTest extends AndysIntegrationTest {

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
        final var sheet = new MusicSheetDto(
            null,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var unauthorised = this.template.postForEntity(
            "/music/sheets", new HttpEntity<>(sheet), MusicSheetDto.class
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED, unauthorised.getStatusCode()
        );
        final var created = this.template.postForEntity(
            "/music/sheets",
            new HttpEntity<>(sheet, this.authenticator.authenticateAsAdmin()),
            MusicSheetDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        Assertions.assertNotNull(created.getBody().getId());
        Assertions.assertEquals(sheet.getTitle(), created.getBody().getTitle());
        final var after = this.template
            .getForEntity("/music/sheets", MusicSheets.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before + 1, after.size());
        Assertions.assertTrue(
            after.stream()
                .anyMatch(item ->
                    item.getId().equals(created.getBody().getId())
                )
        );
    }

    /**
     * Check that we can correctly update a music sheet.
     */
    @Test
    public void testUpdateMusicSheet() {
        final int before = this.template
            .getForEntity("/music/sheets", MusicSheets.class)
            .getBody()
            .getContent()
            .size();
        final var sheet = new MusicSheetDto();
        final var initialTitle = "The music sheet I'm going to edit.";
        sheet.setTitle(initialTitle);
        sheet.setDescription(
            "This is the music sheet that will change it's title."
        );
        sheet.setPreviewUrl("https://whatever.com/m/s/efg");
        sheet.setCreatedOn(new Date());
        final var created = this.template.postForEntity(
            "/music/sheets",
            new HttpEntity<>(sheet, this.authenticator.authenticateAsAdmin()),
            MusicSheetDto.class
        );
        sheet.setId(created.getBody().getId());
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        sheet.setTitle("Some intermediate title");
        template.exchange(
            "/music/sheets",
            HttpMethod.PUT,
            new HttpEntity<>(sheet),
            Void.class
        );
        Assertions.assertEquals(
            initialTitle,
            this.template.getForEntity("/music/sheets", MusicSheets.class)
                .getBody()
                .getContent()
                .stream()
                .filter(sh -> sh.getId().equals(sheet.getId()))
                .findAny().get().getTitle()
        );
        final String newTitle = "Final updated title";
        sheet.setTitle(newTitle);
        final String newDescription =
            "This music sheet description was edited.";
        sheet.setDescription(newDescription);
        final var response = template.exchange(
            "/music/sheets",
            HttpMethod.PUT,
            new HttpEntity<>(sheet, this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        final List<MusicSheetDto> after = this.template
            .getForEntity("/music/sheets", MusicSheets.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before + 1, after.size());
        final var updated = after
            .stream()
            .filter(sh -> sh.getId().equals(sheet.getId()))
            .findAny().get();
        Assertions.assertEquals(newTitle, updated.getTitle());
        Assertions.assertEquals(newDescription, updated.getDescription());
    }

    /**
     * Check that we can correctly delete a music sheet.
     */
    @Test
    public void testDeleteMusicSheet() {
        final int before = this.template
            .getForEntity("/music/sheets", MusicSheets.class)
            .getBody()
            .getContent()
            .size();
        final var sheet = new MusicSheetDto();
        sheet.setTitle("The sheet that I will deleted");
        final var created = this.template.postForEntity(
            "/music/sheets",
            new HttpEntity<>(sheet, this.authenticator.authenticateAsAdmin()),
            MusicSheetDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        final MusicSheetDto found = this.template
            .getForEntity("/music/sheets", MusicSheets.class)
            .getBody()
            .getContent()
            .stream()
            .filter(msheet -> msheet.getTitle().equals(sheet.getTitle()))
            .findAny()
            .get();
        final ResponseEntity<Void> unauthorised = template.exchange(
            String.format("/music/sheets/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(new MultiValueMapAdapter<>(new HashMap<>())),
            Void.class
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED, unauthorised.getStatusCode()
        );
        final ResponseEntity<Void> deleted = template.exchange(
            String.format("/music/sheets/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.OK, deleted.getStatusCode());
        final ResponseEntity<Void> notFound = template.exchange(
            String.format("/music/sheets/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
        final List<MusicSheetDto> after = this.template
            .getForEntity("/music/sheets", MusicSheets.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before, after.size());
        Assertions.assertTrue(
            after.stream()
                .filter(msheet -> msheet.getId().equals(found.getId()))
                .findAny()
                .isEmpty()
        );
    }
}
