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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Video controller.
 *
 * @since 0.1
 */
@RestController
@RequestMapping("/videos")
public final class VideoController {

    /**
     * Just 10.
     */
    private static final int TEN = 10;

    /**
     * Temporal "in memory" storage.
     */
    private final List<VideoDto> storage = new ArrayList<>();

    /**
     * Ctor.
     */
    public VideoController() {
        final var random = new Random();
        for (long i = 0; i < TEN; i++) {
            final var count = random.nextInt(TEN);
            final var tags = new ArrayList<String>(count);
            for (int j = 0; j < count; j++) {
                tags.add(UUID.randomUUID().toString());
            }
            this.storage.add(
                new VideoDto(
                    i + 1,
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    String.format(
                        "https://%s.com/%s",
                        UUID.randomUUID(), UUID.randomUUID()
                    ),
                    new Date(),
                    new Date(),
                    tags
                )
            );
        }
    }

    /**
     * Load videos.
     *
     * @return Videos.
     */
    @GetMapping
    public Videos load() {
        return new Videos(this.storage);
    }

    /**
     * Create a video.
     *
     * @param video Video that needs to be created.
     * @return Created video.
     */
    @PostMapping()
    public ResponseEntity<VideoDto> create(
        @RequestBody final VideoDto video
    ) {
        if (video.getId() == null) {
            video.setId(
                this.storage
                    .stream()
                    .map(VideoDto::getId)
                    .max(Long::compareTo).get() + 1
            );
        }
        this.storage.add(video);
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    /**
     * Edit video.
     *
     * @param video Video that needs to be updated.
     * @return Updated user.
     */
    @PutMapping()
    public ResponseEntity<VideoDto> edit(
        @RequestBody final VideoDto video
    ) {
        final VideoDto found = this.findById(video.getId());
        final int index = this.storage.indexOf(found);
        this.storage.remove(found);
        this.storage.add(index, video);
        return new ResponseEntity<>(
            video, HttpStatus.OK
        );
    }

    /**
     * Delete video.
     *
     * @param id Video ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        this.storage.remove(this.findById(id));
    }

    /**
     * Find video by ID.
     *
     * @param id Video's ID.
     * @return Video.
     */
    private VideoDto findById(final Long id) {
        final Optional<VideoDto> found = this.storage
            .stream()
            .filter(vid -> vid.getId().equals(id))
            .findFirst();
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new IllegalStateException(
                String.format("Video %s does not exist.", id)
            );
        }
    }
}
