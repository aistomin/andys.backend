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
package com.github.aistomin.andys.backend.controllers.photo;

import com.github.aistomin.andys.backend.model.Photo;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Photo DTO.
 *
 * @since 0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString()
@Data
public final class PhotoDto {

    /**
     * Photo ID.
     */
    private Long id;

    /**
     * Photo's description.
     */
    private String description;

    /**
     * Photo URL.
     */
    private String url;

    /**
     * The date when the photo was created.
     */
    private Date createdOn;

    /**
     * The date when the photo was published.
     */
    private Date publishedOn;

    /**
     * Ctor.
     *
     * @param photo Photo entity.
     */
    public PhotoDto(final Photo photo) {
        this(
            photo.getId(),
            photo.getDescription(),
            photo.getUrl(),
            photo.getCreatedOn(),
            photo.getPublishedOn()
        );
    }
}
