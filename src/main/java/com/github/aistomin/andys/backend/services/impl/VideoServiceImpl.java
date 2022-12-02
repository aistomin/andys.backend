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
package com.github.aistomin.andys.backend.services.impl;

import com.github.aistomin.andys.backend.controllers.exceptions.NotFound;
import com.github.aistomin.andys.backend.controllers.video.VideoDto;
import com.github.aistomin.andys.backend.controllers.video.Videos;
import com.github.aistomin.andys.backend.model.Video;
import com.github.aistomin.andys.backend.model.VideoRepository;
import com.github.aistomin.andys.backend.services.VideoService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Video's service's implementation.
 *
 * @since 0.1
 */
@Service
public final class VideoServiceImpl implements VideoService {

    /**
     * User repository.
     */
    private final VideoRepository repo;
    /**
     * Ctor.
     *
     * @param repository User repository.
     */
    public VideoServiceImpl(final VideoRepository repository) {
        this.repo = repository;
    }

    @Override
    public Videos load() {
        return new Videos(
            this.repo.findAll().stream().map(VideoDto::new).toList()
        );
    }

    @Override
    public VideoDto save(final VideoDto video) {
        return new VideoDto(this.repo.save(new Video(video)));
    }

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
                String.format("Video %s does not exist.", id)
            );
        }
    }
}
