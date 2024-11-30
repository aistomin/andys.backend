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
import com.github.aistomin.andys.backend.controllers.video.VideoDto;
import com.github.aistomin.andys.backend.controllers.video.Videos;
import com.github.aistomin.andys.backend.model.Video;
import com.github.aistomin.andys.backend.model.VideoRepository;
import com.github.aistomin.andys.backend.services.VideoService;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Video's service's implementation.
 *
 * @since 0.1
 */
@Service
public class VideoServiceImpl implements VideoService {

    /**
     * Video repository.
     */
    private final VideoRepository repo;

    /**
     * Ctor.
     *
     * @param repository Video repository.
     */
    public VideoServiceImpl(final VideoRepository repository) {
        this.repo = repository;
    }

    /**
     * Load videos. Note: this method loads videos "deeply" with all the nested
     * collections. If you need to load it lazily, you need to create a method
     * for it and adjust this Javadoc :)
     *
     * @return Loaded videos.
     */
    @Transactional
    @Override
    public Videos load() {
        return new Videos(
            this.repo.findAll().stream().map(video -> {
                Hibernate.initialize(video.getSheets());
                Hibernate.initialize(video.getLyrics());
                return new VideoDto(video);
            }).toList()
        );
    }

    /**
     * Save a video.
     *
     * @param video Video that needs to be created.
     * @return Created video.
     */
    @Override
    public VideoDto save(final VideoDto video) {
        return new VideoDto(this.repo.save(new Video(video)));
    }

    /**
     * Delete video.
     *
     * @param id Video ID.
     */
    @Override
    public void delete(final Long id) {
        this.repo.delete(this.findById(id));
    }

    /**
     * Find video by ID.
     *
     * @param id Video's ID.
     * @return Video.
     */
    private Video findById(final Long id) {
        final Optional<Video> found = this.repo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFound(
                String.format("Video with ID = %d not found.", id)
            );
        }
    }
}
