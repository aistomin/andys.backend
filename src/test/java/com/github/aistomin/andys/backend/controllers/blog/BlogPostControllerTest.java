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

import com.github.aistomin.andys.backend.controllers.Authenticator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;

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
        final int before = this.template
            .getForEntity("/blog/posts", BlogPosts.class)
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
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED, unauthorised.getStatusCode()
        );
        final var created = this.template.postForEntity(
            "/blog/posts",
            new HttpEntity<>(post, this.authenticator.authenticateAsAdmin()),
            BlogPostDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
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

    /**
     * Check that we can correctly update a blog post.
     */
    @Test
    public void testUpdateBlogPost() {
        final int before = this.template
            .getForEntity("/blog/posts", BlogPosts.class)
            .getBody()
            .getContent()
            .size();
        final var post = new BlogPostDto();
        final var initialTitle = "The post I'm going to edit.";
        post.setTitle(initialTitle);
        post.setText("This is the post that will change it's title.");
        post.setCreatedOn(new Date());
        post.setPublishedOn(new Date());
        final var created = this.template.postForEntity(
            "/blog/posts",
            new HttpEntity<>(post, this.authenticator.authenticateAsAdmin()),
            BlogPostDto.class
        );
        post.setId(created.getBody().getId());
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        post.setTitle("Some intermediate title");
        template.exchange(
            "/blog/posts",
            HttpMethod.PUT,
            new HttpEntity<>(post),
            Void.class
        );
        Assertions.assertEquals(
            initialTitle,
            this.template.getForEntity("/blog/posts", BlogPosts.class)
                .getBody()
                .getContent()
                .stream()
                .filter(pst -> pst.getId().equals(post.getId()))
                .findAny().get().getTitle()
        );
        final String newTitle = "Final updated title";
        post.setTitle(newTitle);
        final String newText = "This post's text was edited.";
        post.setText(newText);
        final var response = template.exchange(
            "/blog/posts",
            HttpMethod.PUT,
            new HttpEntity<>(post, this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        final List<BlogPostDto> after = this.template
            .getForEntity("/blog/posts", BlogPosts.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before + 1, after.size());
        final var updated = after
            .stream()
            .filter(pst -> pst.getId().equals(post.getId()))
            .findAny().get();
        Assertions.assertEquals(newTitle, updated.getTitle());
        Assertions.assertEquals(newText, updated.getText());
    }

    /**
     * Check that we can correctly delete a blog post.
     */
    @Test
    public void testDeleteBlogPost() {
        final int before = this.template
            .getForEntity("/blog/posts", BlogPosts.class)
            .getBody()
            .getContent()
            .size();
        final var post = new BlogPostDto();
        post.setTitle("Post that I will deleted");
        post.setText("Test post that I will delete later");
        post.setCreatedOn(new Date());
        post.setPublishedOn(new Date());
        final ResponseEntity<BlogPostDto> created = this.template.postForEntity(
            "/blog/posts",
            new HttpEntity<>(post, this.authenticator.authenticateAsAdmin()),
            BlogPostDto.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode());
        final BlogPostDto found = this.template
            .getForEntity("/blog/posts", BlogPosts.class)
            .getBody()
            .getContent()
            .stream()
            .filter(pst -> pst.getTitle().equals(post.getTitle()))
            .findAny()
            .get();
        final ResponseEntity<Void> unauthorised = template.exchange(
            String.format("/blog/posts/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(new MultiValueMapAdapter<>(new HashMap<>())),
            Void.class
        );
        Assertions.assertEquals(
            HttpStatus.UNAUTHORIZED, unauthorised.getStatusCode()
        );
        final ResponseEntity<Void> deleted = template.exchange(
            String.format("/blog/posts/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.OK, deleted.getStatusCode());
        final ResponseEntity<Void> notFound = template.exchange(
            String.format("/blog/posts/%d", found.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(this.authenticator.authenticateAsAdmin()),
            Void.class
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
        final List<BlogPostDto> after = this.template
            .getForEntity("/blog/posts", BlogPosts.class)
            .getBody()
            .getContent();
        Assertions.assertEquals(before, after.size());
        Assertions.assertTrue(
            after.stream()
                .filter(pst -> pst.getId().equals(found.getId()))
                .findAny()
                .isEmpty()
        );
    }
}
