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
package com.github.aistomin.andys.backend.controllers.photo;

import com.github.aistomin.andys.backend.controllers.Authenticator;
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
 * Test for {@link PhotoController}.
 *
 * @since 0.1
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
final class PhotoControllerTest {

    /**
     * Test authenticator.
     */
    @Autowired
    private Authenticator authenticator;

    @Autowired
    private TestRestTemplate template;

    /**
     * Check that we can correctly create a photo.
     */
    @Test
    public void testCreatePhoto() {
        final int before = this.template.getForEntity("/photos", Photos.class)
            .getBody()
            .getContent()
            .size();
        final var photo = new PhotoDto();
        photo.setDescription("This is my new photo.");
        photo.setUrl("https://whatever.com/photo/abc");
        photo.setCreatedOn(new Date());
        photo.setPublishedOn(new Date());
        final ResponseEntity<PhotoDto> unauthorised = this.template.postForEntity(
            "/photos", new HttpEntity<>(photo), PhotoDto.class
        );
        Assertions.assertEquals(401, unauthorised.getStatusCode().value());
        final ResponseEntity<PhotoDto> created = this.template.postForEntity(
            "/photos",
            new HttpEntity<>(photo, this.authenticator.authenticateAsAdmin()),
            PhotoDto.class
        );
        Assertions.assertEquals(201, created.getStatusCode().value());
        final List<PhotoDto> all = this.template.getForEntity("/photos", Photos.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before + 1, all.size());
        final Optional<PhotoDto> found = all.stream()
            .filter(pht -> pht.getDescription().equals(photo.getDescription()))
            .findAny();
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(
            photo.getDescription(), found.get().getDescription()
        );
        Assertions.assertEquals(photo.getUrl(), found.get().getUrl());
        Assertions.assertEquals(
            photo.getCreatedOn(), found.get().getCreatedOn()
        );
        Assertions.assertEquals(
            photo.getPublishedOn(), found.get().getPublishedOn()
        );
    }

    /**
     * Check that we can correctly delete a photo.
     */
    @Test
    public void testDeletePhoto() {
        final int before = this.template.getForEntity("/photos", Photos.class)
            .getBody()
            .getContent()
            .size();
        final var photo = new PhotoDto();
        photo.setDescription("Test photo that I will delete later");
        photo.setUrl("https://whatever.com/photo/cde");
        photo.setCreatedOn(new Date());
        photo.setPublishedOn(new Date());
        final ResponseEntity<PhotoDto> created = this.template.postForEntity(
            "/photos",
            new HttpEntity<>(photo, this.authenticator.authenticateAsAdmin()),
            PhotoDto.class
        );
        Assertions.assertEquals(201, created.getStatusCode().value());
        final PhotoDto found = this.template
            .getForEntity("/photos", Photos.class)
            .getBody()
            .getContent()
            .stream()
            .filter(pht -> pht.getDescription().equals(photo.getDescription()))
            .findAny()
            .get();
        final ResponseEntity<Void> unauthorised = template.exchange(
            String.format("/photos/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(new MultiValueMapAdapter<>(new HashMap<>())),
            Void.class
        );
        Assertions.assertEquals(401, unauthorised.getStatusCode().value());
        final ResponseEntity<Void> deleted = template.exchange(
            String.format("/photos/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(200, deleted.getStatusCode().value());
        final ResponseEntity<Void> notFound = template.exchange(
            String.format("/photos/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(404, notFound.getStatusCode().value());
        final List<PhotoDto> after = this.template
            .getForEntity("/photos", Photos.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before, after.size());
        Assertions.assertTrue(
            after.stream()
                .filter(pht -> pht.getId().equals(found.getId()))
                .findAny()
                .isEmpty()
        );
    }
}
