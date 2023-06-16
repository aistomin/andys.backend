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

import com.github.aistomin.andys.backend.controllers.lyrics.LyricsDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

/**
 * Data object that stores song's lyrics.
 *
 * @since 0.2
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public final class Lyrics {

    /**
     * Length of the lyrics' text.
     */
    private static final int TEXT_LENGTH = 100_000;

    /**
     * Lyrics ID.
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Song's title.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Song's video.
     */
    @ManyToOne
    private Video video;

    /**
     * Lyrics' text.
     */
    @Column(length = Lyrics.TEXT_LENGTH)
    private String text;

    /**
     * The date when the lyrics were created.
     */
    @Column
    private Date createdOn;

    /**
     * The date when the lyrics were published.
     */
    @Column
    private Date publishedOn;

    /**
     * Ctor.
     *
     * @param dto Lyrics DTO.
     */
    public Lyrics(final LyricsDto dto) {
        this(
            dto.getId(),
            dto.getTitle(),
            dto.getVideo() != null ? new Video(dto.getVideo()) : null,
            dto.getText(),
            dto.getCreatedOn(),
            dto.getPublishedOn()
        );
    }
}
