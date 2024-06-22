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
package com.github.aistomin.andys.backend.controllers.music.sheet;

import com.github.aistomin.andys.backend.model.MusicSheet;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;

/**
 * Music sheet DTO.
 *
 * @since 0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString()
@Data
public final class MusicSheetDto {

    /**
     * Music sheet ID.
     */
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Music sheet's title.
     */
    private String title;

    /**
     * Music sheet description.
     */
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
    private Date createdOn;

    /**
     * The date when the music sheet was published.
     */
    private Date publishedOn;

    /**
     * Ctor.
     *
     * @param sheet Music sheet.
     */
    public MusicSheetDto(final MusicSheet sheet) {
        this(
            sheet.getId(),
            sheet.getTitle(),
            sheet.getDescription(),
            sheet.getPreviewUrl(),
            sheet.getDownloadUrl(),
            sheet.getCreatedOn(),
            sheet.getPublishedOn()
        );
    }
}
