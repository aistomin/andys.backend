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
package com.github.aistomin.andys.backend.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access class for {@link Video}.
 *
 * @since 0.1
 */
public interface VideoRepository extends JpaRepository<Video, Long> {

    /**
     * Find a video by the title.
     *
     * @param title The video's title.
     * @return The found video.
     */
    Video findByTitle(String title);
}
