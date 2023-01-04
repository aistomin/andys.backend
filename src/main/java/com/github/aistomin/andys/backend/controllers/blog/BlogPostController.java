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
package com.github.aistomin.andys.backend.controllers.blog;

import com.github.aistomin.andys.backend.services.BlogPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * Create a blog post.
     *
     * @param post Blog post that needs to be created.
     * @return Created blog post.
     */
    @PostMapping()
    public ResponseEntity<BlogPostDto> create(
        @RequestBody final BlogPostDto post
    ) {
        return new ResponseEntity<>(
            this.posts.save(post), HttpStatus.CREATED
        );
    }

    /**
     * Edit a blog post.
     *
     * @param post Blog post that needs to be updated.
     * @return Updated blog post.
     */
    @PutMapping()
    public ResponseEntity<BlogPostDto> edit(
        @RequestBody final BlogPostDto post
    ) {
        return new ResponseEntity<>(
            this.posts.save(post), HttpStatus.OK
        );
    }

    /**
     * Delete blog post.
     *
     * @param id Blog post ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        this.posts.delete(id);
    }
}
