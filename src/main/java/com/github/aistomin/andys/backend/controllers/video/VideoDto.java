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
package com.github.aistomin.andys.backend.controllers.video;

import com.github.aistomin.andys.backend.controllers.music.sheet.MusicSheetDto;
import com.github.aistomin.andys.backend.model.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
     * Music sheets that belong to the video.
     */
    private Set<MusicSheetDto> sheets = new HashSet<>();

    /**
     * The date when the video was created.
     */
    private Date createdOn;

    /**
     * The date when the video was published.
     */
    private Date publishedOn;

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
            video.getSheets()
                .stream()
                .map(MusicSheetDto::new)
                .collect(Collectors.toSet()),
            video.getCreatedOn(),
            video.getPublishedOn()
        );
    }
}
