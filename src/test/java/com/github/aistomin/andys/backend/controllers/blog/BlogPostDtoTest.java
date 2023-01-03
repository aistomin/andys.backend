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
package com.github.aistomin.andys.backend.controllers.blog;

import com.github.aistomin.andys.backend.model.BlogPost;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link BlogPostDto}.
 *
 * @since 0.1
 */
final class BlogPostDtoTest {

    /**
     * Check that we correctly convert {@link BlogPost} to
     * {@link BlogPostDto}.
     */
    @Test
    void testConvert() {
        final var post = new BlogPost(
            new Random().nextLong(1000),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new Date(),
            new Date()
        );
        final var dto = new BlogPostDto(post);
        Assertions.assertEquals(post.getId(), dto.getId());
        Assertions.assertEquals(post.getTitle(), dto.getTitle());
        Assertions.assertEquals(post.getText(), dto.getText());
        Assertions.assertEquals(post.getCreatedOn(), dto.getCreatedOn());
        Assertions.assertEquals(post.getPublishedOn(), dto.getPublishedOn());
    }
}
