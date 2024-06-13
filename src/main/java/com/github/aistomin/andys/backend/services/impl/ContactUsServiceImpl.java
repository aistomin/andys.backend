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
import com.github.aistomin.andys.backend.model.EmailMessageRepository;
import com.github.aistomin.andys.backend.model.EmailMessageType;
import com.github.aistomin.andys.backend.model.Person;
import com.github.aistomin.andys.backend.model.PersonRepository;
import com.github.aistomin.andys.backend.services.ContactUsService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * "Contact Us" service implementation.
 *
 * @since 0.2.
 */
@Service
public final class ContactUsServiceImpl implements ContactUsService {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Person repository.
     */
    private final PersonRepository persons;

    /**
     * Email messages repository.
     */
    private final EmailMessageRepository emails;

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
     * @param personRepository       Person repository.
     * @param emailMessageRepository Email messages repository.
     * @param emailSender            Email sender.
     */
    public ContactUsServiceImpl(
        final PersonRepository personRepository,
        final EmailMessageRepository emailMessageRepository,
        final EmailSender emailSender
    ) {
        this.persons = personRepository;
        this.emails = emailMessageRepository;
        this.sender = emailSender;
    }

    @SuppressWarnings("linelength")
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
                new Person(
                    null, null, null, email, allowToSendNewsLetters, new Date()
                )
            );
        } else {
            dispatcher.setAllowToSendNewsLetters(allowToSendNewsLetters);
            this.persons.save(dispatcher);
        }
        var receptor = this.persons.findByEmail(this.support);
        if (receptor == null) {
            receptor = this.persons.save(
                new Person(
                    null, "Support", "Support", support, true, new Date()
                )
            );
        }
        final var duplicates = this.emails
            .findAllByDispatcherAndReceptorAndSubjectAndBodyAndCreationDateIsAfter(
                dispatcher, receptor, subject,
                body, DateUtils.addWeeks(new Date(), -1)
            );
        if (duplicates.size() == 0) {
            this.sender.sendEmail(
                dispatcher, receptor, subject, body,
                EmailMessageType.CONTACT_REQUEST
            );
        } else {
            this.logger.warn("User {} already sent this message.", email);
        }
    }
}
