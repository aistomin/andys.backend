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
package com.github.aistomin.andys.backend.services.impl;

import com.github.aistomin.andys.backend.controllers.blog.BlogPostDto;
import com.github.aistomin.andys.backend.controllers.blog.BlogPosts;
import com.github.aistomin.andys.backend.controllers.exceptions.NotFound;
import com.github.aistomin.andys.backend.model.BlogPost;
import com.github.aistomin.andys.backend.model.BlogPostRepository;
import com.github.aistomin.andys.backend.services.BlogPostService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Blog posts service's implementation.
 *
 * @since 0.1
 */
@Service
public final class BlogPostServiceImpl implements BlogPostService {

    /**
     * Blog posts repository.
     */
    private final BlogPostRepository repo;

    /**
     * Ctor.
     *
     * @param repository Blog posts repository.
     */
    public BlogPostServiceImpl(final BlogPostRepository repository) {
        this.repo = repository;
    }

    @Override
    public BlogPosts load() {
        return new BlogPosts(
            this.repo.findAll().stream().map(BlogPostDto::new).toList()
        );
    }

    @Override
    public BlogPostDto save(final BlogPostDto post) {
        return new BlogPostDto(this.repo.save(new BlogPost(post)));
    }

    @Override
    public void delete(final Long id) {
        this.repo.delete(this.findById(id));
    }

    /**
     * Find blog post by ID.
     *
     * @param id Blog post's ID.
     * @return Blog post.
     */
    private BlogPost findById(final Long id) {
        final Optional<BlogPost> found = this.repo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFound(
                String.format("Post with ID = %d not found.", id)
            );
        }
    }
}
