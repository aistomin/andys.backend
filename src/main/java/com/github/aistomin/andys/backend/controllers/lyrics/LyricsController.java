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

import com.github.aistomin.andys.backend.services.LyricsService;
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
 * Lyrics controller.
 *
 * @since 0.2
 */
@RestController
@RequestMapping("/lyrics")
public final class LyricsController {

    /**
     * Lyrics service.
     */
    private final LyricsService lyrics;

    /**
     * Ctor.
     *
     * @param service Lyrics service.
     */
    public LyricsController(final LyricsService service) {
        this.lyrics = service;
    }

    /**
     * Load the lyrics catalogue.
     *
     * @return Lyrics.
     */
    @GetMapping
    public LyricsCatalogue load() {
        return this.lyrics.load();
    }

    /**
     * Create lyrics.
     *
     * @param entity Lyrics that need to be created.
     * @return Created lyrics.
     */
    @PostMapping()
    public ResponseEntity<LyricsDto> create(
        @RequestBody final LyricsDto entity
    ) {
        return new ResponseEntity<>(
            this.lyrics.save(entity), HttpStatus.CREATED
        );
    }

    /**
     * Edit lyrics.
     *
     * @param entity Lyrics that need to be updated.
     * @return Updated lyrics.
     */
    @PutMapping()
    public ResponseEntity<LyricsDto> edit(
        @RequestBody final LyricsDto entity
    ) {
        return new ResponseEntity<>(
            this.lyrics.save(entity), HttpStatus.OK
        );
    }

    /**
     * Delete lyrics.
     *
     * @param id Lyrics ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        this.lyrics.delete(id);
    }
}
