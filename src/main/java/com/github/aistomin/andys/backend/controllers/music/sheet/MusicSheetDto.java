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
package com.github.aistomin.andys.backend.controllers.music.sheet;

import com.github.aistomin.andys.backend.model.MusicSheet;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Music sheet DTO.
 *
 * @since 0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString()
@Data
public final class MusicSheetDto {

    /**
     * Music sheet ID.
     */
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
     * Music sheet's public URL.
     */
    private String url;

    /**
     * The date when the music sheet was created.
     */
    private Date createdOn;

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
            sheet.getUrl(),
            sheet.getCreatedOn()
        );
    }
}