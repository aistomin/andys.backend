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

import com.github.aistomin.andys.backend.services.VideoService;
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
     * Video service.
     */
    private final VideoService videos;

    /**
     * Ctor.
     *
     * @param service Video service.
     */
    public VideoController(final VideoService service) {
        this.videos = service;
    }

    /**
     * Load videos.
     *
     * @return Videos.
     */
    @GetMapping
    public Videos load() {
        return this.videos.load();
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
        return new ResponseEntity<>(
            this.videos.save(video), HttpStatus.CREATED
        );
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
        return new ResponseEntity<>(
            this.videos.save(video), HttpStatus.OK
        );
    }

    /**
     * Delete video.
     *
     * @param id Video ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        this.videos.delete(id);
    }
}
