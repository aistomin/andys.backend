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

import com.github.aistomin.andys.backend.services.BlogPostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Blog posts controller.
 *
 * @since 0.1
 */
@RestController
@RequestMapping("/blog/posts")
public final class BlogPostController {

    /**
     * Blog posts service.
     */
    private final BlogPostService posts;

    /**
     * Ctor.
     *
     * @param service Blog posts service.
     */
    public BlogPostController(final BlogPostService service) {
        this.posts = service;
    }

    /**
     * Load blog posts.
     *
     * @return Blog posts.
     */
    @GetMapping
    public BlogPosts load() {
        return this.posts.load();
    }
}
