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

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

/**
 * Data access class for {@link EmailMessage}.
 *
 * @since 0.2
 */
public interface EmailMessageRepository
    extends JpaRepository<EmailMessage, Long> {

    /**
     * Find all the messages by dispatcher.
     *
     * @param dispatcher Dispatcher.
     * @return List of the emails.
     */
    List<EmailMessage> findAllByDispatcher(Person dispatcher);

    /**
     * Find all the identical messages created after certain date.
     *
     * @param dispatcher Dispatcher.
     * @param receptor   Receptor.
     * @param subject    Email's subject.
     * @param body       Email's body.
     * @param date       The date.
     * @return List of emails.
     */
    @SuppressWarnings("linelength")
    List<EmailMessage> findAllByDispatcherAndReceptorAndSubjectAndBodyAndCreationDateIsAfter(
        Person dispatcher,
        Person receptor,
        String subject,
        String body,
        Date date
    );
}
