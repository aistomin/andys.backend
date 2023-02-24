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
package com.github.aistomin.andys.backend.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aistomin.andys.backend.model.EmailMessage;
import com.github.aistomin.andys.backend.model.EmailMessageRepository;
import com.github.aistomin.andys.backend.model.EmailMessageStatus;
import com.github.aistomin.andys.backend.model.EmailMessageType;
import com.github.aistomin.andys.backend.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;

/**
 * Email sender.
 *
 * @since 0.2
 */
@Component
public final class EmailSender {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * JMS template.
     */
    private final JmsTemplate jms;

    /**
     * Email message repository.
     */
    private final EmailMessageRepository emails;

    /**
     * Ctor.
     *
     * @param jmsTemplate JMS template.
     * @param repository  Email message repository.
     */
    public EmailSender(
        final JmsTemplate jmsTemplate,
        final EmailMessageRepository repository
    ) {
        this.jms = jmsTemplate;
        this.emails = repository;
    }

    /**
     * Send the email.
     *
     * @param dispatcher Email dispatcher.
     * @param receptor   Email receptor.
     * @param subject    Email subject.
     * @param body       Email body.
     * @param type       Email type.
     * @return Created Email.
     */
    public EmailMessage sendEmail(
        final Person dispatcher,
        final Person receptor,
        final String subject,
        final String body,
        final EmailMessageType type
    ) {
        final var email = this.emails.save(
            new EmailMessage(
                null, dispatcher, receptor, subject, body,
                EmailMessageStatus.CREATED, type, null, new Date()
            )
        );
        this.jms.send(
            EmailProcessor.EMAIL_QUEUE,
            session -> {
                try {
                    final var data = new HashMap<String, Long>();
                    data.put("email_id", email.getId());
                    return session.createTextMessage(
                        new ObjectMapper().writeValueAsString(data)
                    );
                } catch (final JsonProcessingException error) {
                    logger.error("Error converting to JSON", error);
                    throw new RuntimeException(error);
                }
            }
        );
        return email;
    }
}
