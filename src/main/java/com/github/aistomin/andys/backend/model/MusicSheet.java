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

import com.github.aistomin.andys.backend.controllers.music.sheet.MusicSheetDto;
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
 * Data object that stores music sheet's data.
 *
 * @since 0.1
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public final class MusicSheet {

    /**
     * Length of the music sheet description.
     */
    private static final int DESCRIPTION_LENGTH = 100_000;

    /**
     * Music sheet ID.
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Music sheet's title.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Music sheet's description.
     */
    @Column(length = MusicSheet.DESCRIPTION_LENGTH)
    private String description;

    /**
     * Music sheet's public preview URL.
     */
    @Column
    private String previewUrl;

    /**
     * Music sheet's download URL. In the future it can be public or private.
     */
    @Column
    private String downloadUrl;

    /**
     * The date when the music sheet was created.
     */
    @Column(nullable = false)
    private Date createdOn;

    /**
     * The date when the music sheet was published.
     */
    @Column
    private Date publishedOn;

    /**
     * Ctor.
     *
     * @param dto Music sheet DTO.
     */
    public MusicSheet(final MusicSheetDto dto) {
        this(
            dto.getId(),
            dto.getTitle(),
            dto.getDescription(),
            dto.getPreviewUrl(),
            dto.getDownloadUrl(),
            dto.getCreatedOn(),
            dto.getPublishedOn()
        );
    }
}
