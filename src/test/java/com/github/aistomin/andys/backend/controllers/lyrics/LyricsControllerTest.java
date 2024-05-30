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

import com.github.aistomin.andys.backend.controllers.Authenticator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Test for {@link LyricsController}.
 *
 * @since 0.2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class LyricsControllerTest {

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
     * Check that we can correctly create lyrics.
     */
    @Test
    void testCreateLyrics() {
        final var before = this.template
            .getForEntity("/lyrics", LyricsCatalogue.class)
            .getBody()
            .getContent().size();
        final var lyrics = new LyricsDto(
            null,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var unauthorised = this.template.postForEntity(
            "/lyrics", new HttpEntity<>(lyrics), LyricsDto.class
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED, unauthorised.getStatusCode()
        );
        final var created = this.template.postForEntity(
            "/lyrics",
            new HttpEntity<>(lyrics, this.authenticator.authenticateAsAdmin()),
            LyricsDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        Assertions.assertNotNull(created.getBody().getId());
        Assertions.assertEquals(
            lyrics.getTitle(), created.getBody().getTitle()
        );
        final var after = this.template
            .getForEntity("/lyrics", LyricsCatalogue.class)
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
     * Check that we can correctly update lyrics.
     */
    @Test
    public void testUpdateLyrics() {
        final int before = this.template
            .getForEntity("/lyrics", LyricsCatalogue.class)
            .getBody()
            .getContent()
            .size();
        final var lyrics = new LyricsDto();
        final var initialTitle = "The lyrics I'm going to edit.";
        lyrics.setTitle(initialTitle);
        lyrics.setText(
            "This is the song text: bla-bla."
        );
        lyrics.setCreatedOn(new Date());
        final var created = this.template.postForEntity(
            "/lyrics",
            new HttpEntity<>(lyrics, this.authenticator.authenticateAsAdmin()),
            LyricsDto.class
        );
        lyrics.setId(created.getBody().getId());
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        lyrics.setTitle("Some intermediate title");
        template.exchange(
            "/lyrics",
            HttpMethod.PUT,
            new HttpEntity<>(lyrics),
            Void.class
        );
        Assertions.assertEquals(
            initialTitle,
            this.template.getForEntity("/lyrics", LyricsCatalogue.class)
                .getBody()
                .getContent()
                .stream()
                .filter(sh -> sh.getId().equals(lyrics.getId()))
                .findAny().get().getTitle()
        );
        final String newTitle = "Final updated title";
        lyrics.setTitle(newTitle);
        final String newText =
            "This lyrics text was edited.";
        lyrics.setText(newText);
        final var response = template.exchange(
            "/lyrics",
            HttpMethod.PUT,
            new HttpEntity<>(lyrics, this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        final List<LyricsDto> after = this.template
            .getForEntity("/lyrics", LyricsCatalogue.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before + 1, after.size());
        final var updated = after
            .stream()
            .filter(sh -> sh.getId().equals(lyrics.getId()))
            .findAny().get();
        Assertions.assertEquals(newTitle, updated.getTitle());
        Assertions.assertEquals(newText, updated.getText());
    }

    /**
     * Check that we can correctly delete lyrics.
     */
    @Test
    public void testDeleteLyrics() {
        final int before = this.template
            .getForEntity("/lyrics", LyricsCatalogue.class)
            .getBody()
            .getContent()
            .size();
        final var lyrics = new LyricsDto();
        lyrics.setTitle("The lyrics that I will deleted");
        final var created = this.template.postForEntity(
            "/lyrics",
            new HttpEntity<>(lyrics, this.authenticator.authenticateAsAdmin()),
            LyricsDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        final var found = this.template
            .getForEntity("/lyrics", LyricsCatalogue.class)
            .getBody()
            .getContent()
            .stream()
            .filter(lyr -> lyr.getTitle().equals(lyrics.getTitle()))
            .findAny()
            .get();
        final var unauthorised = template.exchange(
            String.format("/lyrics/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(new MultiValueMapAdapter<>(new HashMap<>())),
            Void.class
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED, unauthorised.getStatusCode()
        );
        final var deleted = template.exchange(
            String.format("/lyrics/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.OK, deleted.getStatusCode());
        final ResponseEntity<Void> notFound = template.exchange(
            String.format("/lyrics/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
        final var after = this.template
            .getForEntity("/lyrics", LyricsCatalogue.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before, after.size());
        Assertions.assertTrue(
            after.stream()
                .filter(lyr -> lyr.getId().equals(found.getId()))
                .findAny()
                .isEmpty()
        );
    }
}
