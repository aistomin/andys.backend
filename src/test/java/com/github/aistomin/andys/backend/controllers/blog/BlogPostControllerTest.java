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

import com.github.aistomin.andys.backend.controllers.Authenticator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

/**
 * Test for {@link BlogPostController}.
 *
 * @since 0.1
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
final class BlogPostControllerTest {

    /**
     * Test authenticator.
     */
    @Autowired
    private Authenticator authenticator;

    /**
     * Test REST template.
     */
    @Autowired
    private TestRestTemplate template;

    /**
     * Check that we can correctly create a blog post.
     */
    @Test
    public void testCreateBlogPost() {
        final int before = this.template.getForEntity("/blog/posts", BlogPosts.class)
            .getBody()
            .getContent()
            .size();
        final var post = new BlogPostDto();
        post.setTitle("My New Post");
        post.setText("This is my new post");
        post.setCreatedOn(new Date());
        post.setPublishedOn(new Date());
        final ResponseEntity<BlogPostDto> unauthorised = this.template
            .postForEntity(
                "/blog/posts", new HttpEntity<>(post), BlogPostDto.class
            );
        Assertions.assertEquals(401, unauthorised.getStatusCode().value());
        final ResponseEntity<BlogPostDto> created = this.template.postForEntity(
            "/blog/posts",
            new HttpEntity<>(post, this.authenticator.authenticateAsAdmin()),
            BlogPostDto.class
        );
        Assertions.assertEquals(201, created.getStatusCode().value());
        final List<BlogPostDto> all = this.template
            .getForEntity("/blog/posts", BlogPosts.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before + 1, all.size());
        final Optional<BlogPostDto> found = all.stream()
            .filter(pst -> pst.getTitle().equals(post.getTitle()))
            .findAny();
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(
            post.getText(), found.get().getText()
        );
        Assertions.assertEquals(
            post.getCreatedOn(), found.get().getCreatedOn()
        );
        Assertions.assertEquals(
            post.getPublishedOn(), found.get().getPublishedOn()
        );
    }
}
