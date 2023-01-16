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

import com.github.aistomin.andys.backend.model.EmailMessageRepository;
import com.github.aistomin.andys.backend.model.EmailMessageStatus;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import org.apache.activemq.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Email processing logic.
 *
 * @since 0.2
 */
@Component
public final class EmailProcessor {

    /**
     * Email queue.
     */
    public static final String EMAIL_QUEUE = "email.send";

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Email message repository.
     */
    @Autowired
    private EmailMessageRepository repository;

    /**
     * Receive AMQ message.
     *
     * @param message AMQ message.
     * @throws JMSException If something went wrong.
     */
    @JmsListener(destination = EMAIL_QUEUE)
    public void receiveMessage(final Message message) throws JMSException {
        logger.debug("Message received: {}", message);
        if (message instanceof TextMessage msg) {
            final var json = JsonParserFactory.getJsonParser()
                .parseMap(msg.getText());
            final var id = Long.parseLong(json.get("email_id").toString());
            final var email = this.repository.findById(id).get();
            if (email.getReceptor().getEmail().endsWith("failed.email")) {
                email.setStatus(EmailMessageStatus.FAILED);
                logger.error("Email {} is failed.", email.getId());
            } else {
                email.setStatus(EmailMessageStatus.SENT);
                logger.debug("Email {} is sent.", email.getId());
            }
            this.repository.save(email);
        } else {
            logger.error("No idea what is that: {}", message);
        }
    }
}
