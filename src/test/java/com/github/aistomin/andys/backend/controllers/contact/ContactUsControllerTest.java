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
package com.github.aistomin.andys.backend.controllers.contact;

import com.github.aistomin.andys.backend.model.EmailMessageRepository;
import com.github.aistomin.andys.backend.model.PersonRepository;
import com.github.aistomin.andys.backend.utils.AndysIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

/**
 * Test for {@link ContactUsController}.
 *
 * @since 0.2
 */
class ContactUsControllerTest extends AndysIntegrationTest {

    /**
     * Test REST template.
     */
    @Autowired
    private TestRestTemplate template;

    /**
     * Person repository.
     */
    @Autowired
    private PersonRepository persons;

    /**
     * Emails repository.
     */
    @Autowired
    private EmailMessageRepository emails;

    /**
     * Check that we correctly process "Contact Us" request.
     */
    @Test
    void testContactUs() {
        final var api = "/contact/us";
        final var request = new ContactRequest(
            "customer@mailinator.com",
            "Hello support!",
            "Hello guys, when do you plan to create a new content???",
            true
        );
        Assertions.assertNull(this.persons.findByEmail(request.getEmail()));
        Assertions.assertEquals(
            HttpStatus.CREATED,
            this.template.postForEntity(
                api, new HttpEntity<>(request), String.class
            ).getStatusCode()
        );
        final var customer = this.persons.findByEmail(request.getEmail());
        Assertions.assertNotNull(customer);
        Assertions.assertTrue(customer.getAllowToSendNewsLetters());
        final var mails = this.emails.findAllByDispatcher(customer);
        Assertions.assertEquals(1, mails.size());
        Assertions.assertEquals(
            request.getSubject(), mails.get(0).getSubject()
        );
        Assertions.assertEquals(request.getBody(), mails.get(0).getBody());
        Assertions.assertNotNull(
            this.persons.findByEmail("support@mailinator.com")
        );
        Assertions.assertEquals(
            HttpStatus.CREATED,
            this.template.postForEntity(
                api, new HttpEntity<>(
                    new ContactRequest(
                        "customer@mailinator.com",
                        "Hello support!",
                        "Hello guys, stop spamming me with your newsletters!!!",
                        false
                    )
                ), String.class
            ).getStatusCode()
        );
        Assertions.assertFalse(
            this.persons.findByEmail(request.getEmail())
                .getAllowToSendNewsLetters()
        );
        Assertions.assertEquals(
            mails.size() + 1,
            this.emails.findAllByDispatcher(customer).size()
        );
    }

    /**
     * Check that we correctly process "Contact Us" request when user sends the
     * same message over and over again.
     */
    @Test
    void testContinuousRequests() {
        final var api = "/contact/us";
        final var request = new ContactRequest(
            "insistant.dude@mailinator.com",
            "Hey you!",
            "I will be spamming you!",
            true
        );
        Assertions.assertEquals(
            HttpStatus.CREATED,
            this.template.postForEntity(
                api, new HttpEntity<>(request), String.class
            ).getStatusCode()
        );
        final var customer = this.persons.findByEmail(request.getEmail());
        final var mails = this.emails.findAllByDispatcher(customer);
        Assertions.assertEquals(1, mails.size());
        Assertions.assertEquals(
            HttpStatus.CREATED,
            this.template.postForEntity(
                api, new HttpEntity<>(request), String.class
            ).getStatusCode()
        );
        Assertions.assertEquals(
            HttpStatus.CREATED,
            this.template.postForEntity(
                api, new HttpEntity<>(request), String.class
            ).getStatusCode()
        );
        Assertions.assertEquals(1, mails.size());
    }
}
