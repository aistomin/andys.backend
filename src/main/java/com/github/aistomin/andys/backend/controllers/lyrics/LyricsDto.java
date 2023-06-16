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
package com.github.aistomin.andys.backend.controllers.lyrics;

import com.github.aistomin.andys.backend.controllers.video.VideoDto;
import com.github.aistomin.andys.backend.model.Lyrics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;

/**
 * Lyrics DTO.
 *
 * @since 0.2
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString()
@Data
public final class LyricsDto {

    /**
     * Lyrics ID.
     */
    private Long id;

    /**
     * Lyrics's title.
     */
    private String title;

    /**
     * Video with the music.
     */
    private VideoDto video;

    /**
     * Lyrics description.
     */
    private String text;

    /**
     * The date when the Lyrics were created.
     */
    private Date createdOn;

    /**
     * The date when the Lyrics were published.
     */
    private Date publishedOn;

    /**
     * Ctor.
     *
     * @param lyrics Lyrics.
     */
    public LyricsDto(final Lyrics lyrics) {
        this(
            lyrics.getId(),
            lyrics.getTitle(),
            lyrics.getVideo() != null ? new VideoDto(lyrics.getVideo()) : null,
            lyrics.getText(),
            lyrics.getCreatedOn(),
            lyrics.getPublishedOn()
        );
    }
}
