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

import com.github.aistomin.andys.backend.model.EmailMessage;
import com.github.aistomin.andys.backend.model.EmailMessageRepository;
import com.github.aistomin.andys.backend.model.EmailMessageStatus;
import com.github.aistomin.andys.backend.model.EmailMessageType;
import com.github.aistomin.andys.backend.model.Person;
import com.github.aistomin.andys.backend.model.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;

/**
 * Test for {@link EmailProcessor} and {@link EmailSender}.
 *
 * @since 0.2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
final class EmailProcessorTest {

    /**
     * Sleep timeout.
     */
    private static final Long TIMEOUT = 3_000L;

    /**
     * Person repository.
     */
    @Autowired
    private PersonRepository persons;

    /**
     * Email message repository.
     */
    @Autowired
    private EmailMessageRepository emails;

    /**
     * Email sender.
     */
    @Autowired
    private EmailSender sender;

    /**
     * Check that we are correctly sending emails.
     *
     * @throws InterruptedException If something goes wrong.
     */
    @Test
    void testProcessEmail() throws InterruptedException {
        final Person john = this.persons.save(
            new Person(
                null,
                "John",
                "Doe",
                "test@successful.email",
                true,
                new Date()
            )
        );
        final Person max = this.persons.save(
            new Person(
                null,
                "Max",
                "Mustermann",
                "test@failed.email",
                true,
                new Date()
            )
        );
        final EmailMessage failed = this.sender.sendEmail(
            john, max, "Failed email", "Hello Max!",
            EmailMessageType.NEWS_LETTER
        );
        Thread.sleep(TIMEOUT);
        Assertions.assertEquals(
            EmailMessageStatus.FAILED,
            emails.findById(failed.getId()).get().getStatus()
        );
        final EmailMessage successful = this.sender.sendEmail(
            max, john, "Successful email",
            "Hello John! Haven't heard from you a long time!",
            EmailMessageType.NEWS_LETTER
        );
        Thread.sleep(TIMEOUT);
        Assertions.assertEquals(
            EmailMessageStatus.SENT,
            emails.findById(successful.getId()).get().getStatus()
        );
    }
}
