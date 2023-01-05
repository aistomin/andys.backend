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

import com.github.aistomin.andys.backend.services.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Photo controller.
 *
 * @since 0.1
 */
@RestController
@RequestMapping("/photos")
public final class PhotoController {

    /**
     * Photo service.
     */
    private final PhotoService photos;

    /**
     * Ctor.
     *
     * @param service Photo service.
     */
    public PhotoController(final PhotoService service) {
        this.photos = service;
    }

    /**
     * Load photos.
     *
     * @return Photos.
     */
    @GetMapping
    public Photos load() {
        return this.photos.load();
    }

    /**
     * Create a photo.
     *
     * @param photo Photo that needs to be created.
     * @return Created photo.
     */
    @PostMapping()
    public ResponseEntity<PhotoDto> create(
        @RequestBody final PhotoDto photo
    ) {
        return new ResponseEntity<>(
            this.photos.save(photo), HttpStatus.CREATED
        );
    }

    /**
     * Delete photo.
     *
     * @param id Photo ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        this.photos.delete(id);
    }
}
