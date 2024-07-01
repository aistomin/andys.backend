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

import jakarta.transaction.Transactional;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

/**
 * Test for {@link VideoRepository}.
 *
 * @since 0.2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
final class VideoRepositoryTest {

    /**
     * Videos repository.
     */
    @Autowired
    private VideoRepository videos;

    /**
     * Music sheets repository.
     */
    @Autowired
    private MusicSheetRepository sheets;

    /**
     * Lyrics repository.
     */
    @Autowired
    private LyricsRepository lyrics;

    /**
     * Check that we can correctly save the video.
     */
    @Test
    @Transactional
    void testSaveVideo() {
        final var before = this.videos.findAll().size();
        final var title = "My first Video";
        final var description = "This is a nice video about my life.";
        final var url = "https://videoserver.de/bla-bla-id";
        final var youtubeId = "bla-bla-id";
        final var date = new Date();
        final var published = new Date();
        final var notes = new HashSet<MusicSheet>() {{
            add(createMusicSheet());
            add(createMusicSheet());
            add(createMusicSheet());
        }};
        final var texts = new HashSet<Lyrics>() {{
            add(createLyrics());
            add(createLyrics());
        }};
        this.videos.save(
            new Video(
                null,
                title,
                description,
                url,
                youtubeId,
                notes,
                texts,
                date,
                published
            )
        );
        final var video = this.videos
            .findAll()
            .stream()
            .max(Comparator.comparing(Video::getId))
            .get();
        Assertions.assertEquals(title, video.getTitle());
        Assertions.assertEquals(description, video.getDescription());
        Assertions.assertEquals(url, video.getUrl());
        Assertions.assertEquals(youtubeId, video.getYoutubeId());
        Assertions.assertEquals(date, video.getCreatedOn());
        Assertions.assertEquals(published, video.getPublishedOn());
        Assertions.assertEquals(notes.size(), video.getSheets().size());
        notes.forEach(sheet ->
            Assertions.assertTrue(
                video.getSheets()
                    .stream()
                    .map(MusicSheet::getTitle)
                    .toList()
                    .contains(sheet.getTitle())
            )
        );
        Assertions.assertEquals(texts.size(), video.getLyrics().size());
        texts.forEach(text ->
            Assertions.assertTrue(
                video.getLyrics()
                    .stream()
                    .map(Lyrics::getTitle)
                    .toList()
                    .contains(text.getTitle())
            )
        );
        Assertions.assertEquals(before + 1, this.videos.findAll().size());
    }

    /**
     * Check that we do not allow to save videos without "createdOn" value.
     */
    @Test
    void testCreatedOn() {
        Video video = new Video(
            null,
            "My second Video",
            "This is a nice video about my death.",
            "https://videoserver.de/bla-bla-id",
            "bla-bla-id",
            null,
            null,
            null,
            null
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.videos.save(video)
        );
        video.setCreatedOn(new Date());
        this.videos.save(video);
    }

    /**
     * The Video entity is a relatively "rich" thing, and it has some
     * collections, that logically belong to it. In this test I want to make
     * sure that we do not select all the data from database, but all the
     * dependent collections are loaded on demand. The test is executed in the
     * non-transactional context, so any call to the sub-collections should end
     * up with the {@link LazyInitializationException}. The other tests are
     * checking that the collections can be actually loaded in the transactional
     * context.
     */
    @Test
    void testLazyLoadingOfTheDependentEntities() {
        final var title = "My second Video";
        this.videos.save(
            new Video(
                null,
                title,
                "This is a nice video about damn Hibernate.",
                "https://videoserver.de/hibernate-ist-doof",
                "hibernate-ist-doof",
                new HashSet<>() {{
                    add(createMusicSheet());
                    add(createMusicSheet());
                    add(createMusicSheet());
                }},
                new HashSet<>() {{
                    add(createLyrics());
                    add(createLyrics());
                }},
                new Date(),
                new Date()
            )
        );
        final var video = this.videos.findByTitle(title);
        Assertions.assertThrows(
            LazyInitializationException.class,
            () -> {
                video.getLyrics().size();
            }
        );
        Assertions.assertThrows(
            LazyInitializationException.class,
            () -> {
                video.getSheets().size();
            }
        );
    }

    /**
     * Create a test music sheet with random data.
     *
     * @return Music sheet.
     */
    private MusicSheet createMusicSheet() {
        return this.sheets.save(
            new MusicSheet(
                null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                new Date()
            )
        );
    }

    /**
     * Create lyrics with random data.
     *
     * @return Lyrics.
     */
    private Lyrics createLyrics() {
        return this.lyrics.save(
            new Lyrics(
                null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                new Date()
            )
        );
    }
}
