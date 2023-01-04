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
package com.github.aistomin.andys.backend.services;

import com.github.aistomin.andys.backend.controllers.blog.BlogPostDto;
import com.github.aistomin.andys.backend.controllers.blog.BlogPosts;

/**
 * Blog post's service.
 *
 * @since 0.1
 */
public interface BlogPostService {

    /**
     * Load blog posts.
     *
     * @return Blog posts.
     */
    BlogPosts load();

    /**
     * Save a blog post.
     *
     * @param post Blog post that needs to be created.
     * @return Created blog post.
     */
    BlogPostDto save(BlogPostDto post);

    /**
     * Delete blog post.
     *
     * @param id Blog post ID.
     */
    void delete(Long id);
}
