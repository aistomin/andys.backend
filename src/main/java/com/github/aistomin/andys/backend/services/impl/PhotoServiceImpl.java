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
package com.github.aistomin.andys.backend.services.impl;

import com.github.aistomin.andys.backend.controllers.exceptions.NotFound;
import com.github.aistomin.andys.backend.controllers.photo.PhotoDto;
import com.github.aistomin.andys.backend.controllers.photo.Photos;
import com.github.aistomin.andys.backend.model.Photo;
import com.github.aistomin.andys.backend.model.PhotoRepository;
import com.github.aistomin.andys.backend.services.PhotoService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Photo's service's implementation.
 *
 * @since 0.1
 */
@Service
public final class PhotoServiceImpl implements PhotoService {

    /**
     * Photo repository.
     */
    private final PhotoRepository repo;

    /**
     * Ctor.
     *
     * @param repository Photo repository.
     */
    public PhotoServiceImpl(final PhotoRepository repository) {
        this.repo = repository;
    }

    @Override
    public Photos load() {
        return new Photos(
            this.repo.findAll().stream().map(PhotoDto::new).toList()
        );
    }

    @Override
    public PhotoDto save(final PhotoDto photo) {
        return new PhotoDto(this.repo.save(new Photo(photo)));
    }

    @Override
    public void delete(final Long id) {
        this.repo.delete(this.findById(id));
    }

    /**
     * Find photo by ID.
     *
     * @param id Photo's ID.
     * @return Photo.
     */
    private Photo findById(final Long id) {
        final Optional<Photo> found = this.repo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFound(
                String.format("Photo with ID = %d not found.", id)
            );
        }
    }
}
