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

import com.github.aistomin.andys.backend.controllers.blog.BlogPostDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;

/**
 * Data object that stores blog post's data.
 *
 * @since 0.1
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public final class BlogPost {

    /**
     * Length of the post.
     */
    private static final int POST_LENGTH = 100_000;

    /**
     * Post ID.
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Post's title.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Post's text.
     */
    @Column(length = POST_LENGTH)
    private String text;

    /**
     * The date when the blog post was created.
     */
    @Column(nullable = false)
    private Date createdOn;

    /**
     * The date when the post was published.
     */
    @Column
    private Date publishedOn;

    /**
     * Ctor.
     *
     * @param dto Blog post DTO.
     */
    public BlogPost(final BlogPostDto dto) {
        this(
            dto.getId(),
            dto.getTitle(),
            dto.getText(),
            dto.getCreatedOn(),
            dto.getPublishedOn()
        );
    }
}
