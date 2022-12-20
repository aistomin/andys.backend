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

import com.github.aistomin.andys.backend.model.Video;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Video DTO.
 *
 * @since 0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString()
@Data
public final class VideoDto {

    /**
     * Video ID.
     */
    private Long id;

    /**
     * Video's title.
     */
    private String title;

    /**
     * Description.
     */
    private String description;

    /**
     * Video URL.
     */
    private String url;

    /**
     * The date when the video was created.
     */
    private Date createdOn;

    /**
     * The date when the video was published.
     */
    private Date publishedOn;

    /**
     * Video hashtags.
     */
    private List<String> tags;

    /**
     * Ctor.
     *
     * @param video Video entity.
     */
    public VideoDto(final Video video) {
        this(
            video.getId(),
            video.getTitle(),
            video.getDescription(),
            video.getUrl(),
            video.getCreatedOn(),
            video.getPublishedOn(),
            video.getTags()
        );
    }
}
