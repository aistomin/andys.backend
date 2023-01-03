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
package com.github.aistomin.andys.backend.controllers.video;

import com.github.aistomin.andys.backend.controllers.Authenticator;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;

/**
 * Test for {@link VideoController}.
 *
 * @since 0.1
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class VideoControllerTest {

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
     * Check that we can correctly create a video.
     */
    @Test
    public void testCreateVideo() {
        final int before = this.template.getForEntity("/videos", Videos.class)
            .getBody()
            .getContent()
            .size();
        final var video = new VideoDto();
        video.setTitle("My New Video");
        video.setDescription("This is my new video");
        video.setUrl("https://whatever.com/video/abc");
        video.setCreatedOn(new Date());
        video.setPublishedOn(new Date());
        video.setTags(Arrays.asList("Nature", "Dogs", "Pets"));
        final ResponseEntity<VideoDto> unauthorised = this.template.postForEntity(
            "/videos", new HttpEntity<>(video), VideoDto.class
        );
        Assertions.assertEquals(401, unauthorised.getStatusCode().value());
        final ResponseEntity<VideoDto> created = this.template.postForEntity(
            "/videos",
            new HttpEntity<>(video, this.authenticator.authenticateAsAdmin()),
            VideoDto.class
        );
        Assertions.assertEquals(201, created.getStatusCode().value());
        final List<VideoDto> all = this.template.getForEntity("/videos", Videos.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before + 1, all.size());
        final Optional<VideoDto> found = all.stream()
            .filter(vid -> vid.getTitle().equals(video.getTitle()))
            .findAny();
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(
            video.getDescription(), found.get().getDescription()
        );
        Assertions.assertEquals(video.getUrl(), found.get().getUrl());
        Assertions.assertEquals(
            video.getCreatedOn(), found.get().getCreatedOn()
        );
        Assertions.assertEquals(
            video.getPublishedOn(), found.get().getPublishedOn()
        );
        Assertions.assertEquals(
            video.getTags().size(), found.get().getTags().size()
        );
        for (final String tag : video.getTags()) {
            Assertions.assertTrue(found.get().getTags().contains(tag));
        }
    }

    /**
     * Check that we can correctly update a video.
     */
    @Test
    public void testUpdateVideo() {
        final int before = this.template.getForEntity("/videos", Videos.class)
            .getBody()
            .getContent()
            .size();
        final var video = new VideoDto();
        final var initialTitle = "The Video I'm going to edit.";
        video.setTitle(initialTitle);
        video.setDescription("This is the video that will change it's title.");
        video.setUrl("https://whatever.com/video/efg");
        video.setCreatedOn(new Date());
        video.setPublishedOn(new Date());
        video.setTags(Arrays.asList("Video", "Editing"));
        final var created = this.template.postForEntity(
            "/videos",
            new HttpEntity<>(video, this.authenticator.authenticateAsAdmin()),
            VideoDto.class
        );
        video.setId(created.getBody().getId());
        Assertions.assertEquals(201, created.getStatusCode().value());
        video.setTitle("Some intermediate title");
        template.exchange(
            "/videos",
            HttpMethod.PUT,
            new HttpEntity<>(video),
            Void.class
        );
        Assertions.assertEquals(
            initialTitle,
            this.template.getForEntity("/videos", Videos.class)
                .getBody()
                .getContent()
                .stream()
                .filter(vid -> vid.getId().equals(video.getId()))
                .findAny().get().getTitle()
        );
        final String newTitle = "Final updated title";
        video.setTitle(newTitle);
        final String newDescription = "This video description was edited.";
        video.setDescription(newDescription);
        final var response = template.exchange(
            "/videos",
            HttpMethod.PUT,
            new HttpEntity<>(video, this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(200, response.getStatusCode().value());
        final List<VideoDto> after = this.template.getForEntity("/videos", Videos.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before + 1, after.size());
        final var updated = after
            .stream()
            .filter(vid -> vid.getId().equals(video.getId()))
            .findAny().get();
        Assertions.assertEquals(newTitle, updated.getTitle());
        Assertions.assertEquals(newDescription, updated.getDescription());
    }

    /**
     * Check that we can correctly delete a video.
     */
    @Test
    public void testDeleteVideo() {
        final int before = this.template.getForEntity("/videos", Videos.class)
            .getBody()
            .getContent()
            .size();
        final var video = new VideoDto();
        video.setTitle("Video that I will deleted");
        video.setDescription("Test video that I will delete later");
        video.setUrl("https://whatever.com/video/cde");
        video.setCreatedOn(new Date());
        video.setPublishedOn(new Date());
        video.setTags(Arrays.asList("Video", "testing", "deletion"));
        final ResponseEntity<VideoDto> created = this.template.postForEntity(
            "/videos",
            new HttpEntity<>(video, this.authenticator.authenticateAsAdmin()),
            VideoDto.class
        );
        Assertions.assertEquals(201, created.getStatusCode().value());
        final VideoDto found = this.template
            .getForEntity("/videos", Videos.class)
            .getBody()
            .getContent()
            .stream()
            .filter(vid -> vid.getTitle().equals(video.getTitle()))
            .findAny()
            .get();
        final ResponseEntity<Void> unauthorised = template.exchange(
            String.format("/videos/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(new MultiValueMapAdapter<>(new HashMap<>())),
            Void.class
        );
        Assertions.assertEquals(401, unauthorised.getStatusCode().value());
        final ResponseEntity<Void> deleted = template.exchange(
            String.format("/videos/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(200, deleted.getStatusCode().value());
        final ResponseEntity<Void> notFound = template.exchange(
            String.format("/videos/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(404, notFound.getStatusCode().value());
        final List<VideoDto> after = this.template
            .getForEntity("/videos", Videos.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before, after.size());
        Assertions.assertTrue(
            after.stream()
                .filter(vid -> vid.getId().equals(found.getId()))
                .findAny()
                .isEmpty()
        );
    }
}
