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

import com.github.aistomin.andys.backend.controllers.video.VideoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

/**
 * Data object that stores video's data.
 *
 * @since 0.1
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public final class Video {

    /**
     * Length of the video description.
     */
    private static final int DESCRIPTION_LENGTH = 100_000;

    /**
     * Video ID.
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Video's title.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Video's description.
     */
    @Column(length = Video.DESCRIPTION_LENGTH)
    private String description;

    /**
     * Video URL.
     */
    @Column
    private String url;

    /**
     * The date when the video was created.
     */
    @Column
    private Date createdOn;

    /**
     * The date when the video was published.
     */
    @Column
    private Date publishedOn;

    /**
     * Ctor.
     *
     * @param dto Video DTO.
     */
    public Video(final VideoDto dto) {
        this(
            dto.getId(),
            dto.getTitle(),
            dto.getDescription(),
            dto.getUrl(),
            dto.getCreatedOn(),
            dto.getPublishedOn()
        );
    }
}
