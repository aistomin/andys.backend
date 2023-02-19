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

import com.github.aistomin.andys.backend.activemq.EmailSender;
import com.github.aistomin.andys.backend.model.EmailMessageType;
import com.github.aistomin.andys.backend.model.Person;
import com.github.aistomin.andys.backend.model.PersonRepository;
import com.github.aistomin.andys.backend.services.ContactUsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * "Contact Us" service implementation.
 *
 * @since 0.2.
 */
@Service
public final class ContactUsServiceImpl implements ContactUsService {

    /**
     * Person repository.
     */
    private final PersonRepository persons;

    /**
     * Email sender.
     */
    private final EmailSender sender;

    /**
     * Support email.
     */
    @Value("${support.email}")
    private String support;

    /**
     * Ctor.
     *
     * @param personRepository Person repository.
     * @param emailSender      Email sender.
     */
    public ContactUsServiceImpl(
        final PersonRepository personRepository, final EmailSender emailSender
    ) {
        this.persons = personRepository;
        this.sender = emailSender;
    }

    @Override
    public void contactUs(
        final String email,
        final String subject,
        final String body,
        final Boolean allowToSendNewsLetters
    ) {
        var dispatcher = this.persons.findByEmail(email);
        if (dispatcher == null) {
            dispatcher = this.persons.save(
                new Person(null, null, null, email, allowToSendNewsLetters)
            );
        } else {
            dispatcher.setAllowToSendNewsLetters(allowToSendNewsLetters);
            this.persons.save(dispatcher);
        }
        var receptor = this.persons.findByEmail(this.support);
        if (receptor == null) {
            receptor = this.persons.save(
                new Person(null, "Support", "Support", support, true)
            );
        }
        this.sender.sendEmail(
            dispatcher, receptor, subject, body,
            EmailMessageType.CONTACT_REQUEST
        );
    }
}
