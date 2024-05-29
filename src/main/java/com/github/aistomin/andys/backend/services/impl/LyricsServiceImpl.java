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
import com.github.aistomin.andys.backend.controllers.lyrics.LyricsCatalogue;
import com.github.aistomin.andys.backend.controllers.lyrics.LyricsDto;
import com.github.aistomin.andys.backend.model.Lyrics;
import com.github.aistomin.andys.backend.model.LyricsRepository;
import com.github.aistomin.andys.backend.services.LyricsService;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Lyrics' service's implementation.
 *
 * @since 0.2
 */
@Service
public final class LyricsServiceImpl implements LyricsService {

    /**
     * Lyrics repository.
     */
    private final LyricsRepository repo;

    /**
     * Ctor.
     *
     * @param repository Lyrics repository.
     */
    public LyricsServiceImpl(final LyricsRepository repository) {
        this.repo = repository;
    }

    @Override
    public LyricsCatalogue load() {
        return new LyricsCatalogue(
            this.repo.findAll().stream().map(LyricsDto::new).toList()
        );
    }

    @Override
    public LyricsDto save(final LyricsDto lyrics) {
        return new LyricsDto(this.repo.save(new Lyrics(lyrics)));
    }

    @Override
    public void delete(final Long id) {
        this.repo.delete(this.findById(id));
    }

    /**
     * Find lyrics by ID.
     *
     * @param id Lyrics' ID.
     * @return Lyrics.
     */
    private Lyrics findById(final Long id) {
        final Optional<Lyrics> found = this.repo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFound(
                String.format("Lyrics with ID = %d not found.", id)
            );
        }
    }
}
