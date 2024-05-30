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
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Column(nullable = false)
    private String url;

    /**
     * Video's ID on the YouTube platform.
     */
    @Column(nullable = false)
    private String youtubeId;

    /**
     * Music sheets that belong to the video.
     * @todo: Issue #414 Fix the fetch type.
     */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<MusicSheet> sheets = new HashSet<>();

    /**
     * Lyrics that belong to the video.
     * @todo: Issue #414 Fix the fetch type.
     */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Lyrics> lyrics = new HashSet<>();

    /**
     * The date when the video was created.
     */
    @Column(nullable = false)
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
            dto.getYoutubeId(),
            dto.getSheets()
                .stream()
                .map(MusicSheet::new)
                .collect(Collectors.toSet()),
            dto.getLyrics()
                .stream()
                .map(Lyrics::new)
                .collect(Collectors.toSet()),
            dto.getCreatedOn(),
            dto.getPublishedOn()
        );
    }
}
