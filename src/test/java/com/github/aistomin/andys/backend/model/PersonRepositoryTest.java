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

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

/**
 * Test for {@link PersonRepository}.
 *
 * @since 0.2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
final class PersonRepositoryTest {

    /**
     * Person repository.
     */
    @Autowired
    private PersonRepository repository;

    /**
     * Check that we can correctly save the user.
     */
    @Test
    void testSavePerson() {
        final var before = this.repository.findAll().size();
        final String email = "andy@grails.test";
        final String name = "Andy";
        final String surname = "Grails";
        final Boolean allowNews = new Random().nextBoolean();
        this.repository.save(
            new Person(null, name, surname, email, allowNews, new Date())
        );
        final var andy = this.repository
            .findAll()
            .stream()
            .max(Comparator.comparing(Person::getId))
            .get();
        Assertions.assertEquals(name, andy.getFirstName());
        Assertions.assertEquals(surname, andy.getLastName());
        Assertions.assertEquals(email, andy.getEmail());
        Assertions.assertEquals(allowNews, andy.getAllowToSendNewsLetters());
        Assertions.assertNotNull(andy.getCreatedOn());
        final var john = new Person(
            null, "John", "Doe", email, false, new Date()
        );
        Assertions.assertThrows(
            DataIntegrityViolationException.class,
            () -> this.repository.save(john)
        );
        john.setEmail("bla-bla-bla");
        Assertions.assertThrows(
            ConstraintViolationException.class,
            () -> this.repository.save(john)
        );
        john.setEmail("pseudo.valid@mail.com");
        this.repository.save(john);
        Assertions.assertEquals(before + 2, this.repository.findAll().size());
    }
}
