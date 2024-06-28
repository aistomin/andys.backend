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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;

/**
 * Test for {@link EmailMessageRepository}.
 *
 * @since 0.2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
final class EmailMessageRepositoryTest {

    /**
     * Email message repository.
     */
    @Autowired
    private EmailMessageRepository emails;

    /**
     * Person repository.
     */
    @Autowired
    private PersonRepository persons;

    /**
     * Check that we correctly save the email message.
     */
    @SuppressWarnings("checkstyle:LineLength")
    @Test
    void testSaveEmail() {
        final var clara = this.persons.save(
            new Person(
                null, "Clara", "Zetkin", "clara@hotgirls.com", true, new Date()
            )
        );
        final var karl = this.persons.save(
            new Person(
                null,
                "Karl",
                "Marx",
                "wanna_see_my_manifest@redboys.com",
                true,
                new Date()
            )
        );
        final String subject = "Manifest der Kommunistischen Partei";
        final String body = "Die Geschichte aller bisherigen Gesellschaft ist die Geschichte von Klassenkämpfen. Freier und Sklave, Patrizier und Plebejer, Baron und Leibeigener, Zunftbürger und Gesell, kurz, Unterdrücker und Unterdrückte standen in stetem Gegensatz zueinander, führten einen ununterbrochenen, bald versteckten, bald offenen Kampf, einen Kampf, der jedesmal mit einer revolutionären Umgestaltung der ganzen Gesellschaft endete oder mit dem gemeinsamen Untergang der kämpfenden Klassen.";
        final var email = this.emails.save(
            new EmailMessage(
                null,
                karl,
                clara,
                subject,
                body,
                EmailMessageStatus.CREATED,
                EmailMessageType.NEWS_LETTER,
                null,
                new Date()
            )
        );
        Assertions.assertEquals(karl.getId(), email.getDispatcher().getId());
        Assertions.assertEquals(clara.getId(), email.getReceptor().getId());
        Assertions.assertEquals(subject, email.getSubject());
        Assertions.assertEquals(body, email.getBody());
        Assertions.assertEquals(EmailMessageStatus.CREATED, email.getStatus());
        Assertions.assertEquals(EmailMessageType.NEWS_LETTER, email.getType());
        Assertions.assertNotNull(email.getCreatedOn());
        Assertions.assertNull(email.getInfo());
        final var info = "SUCCESS: OK";
        email.setStatus(EmailMessageStatus.SENT);
        email.setInfo(info);
        final var updated = this.emails.save(email);
        Assertions.assertEquals(EmailMessageStatus.SENT, updated.getStatus());
        Assertions.assertEquals(info, updated.getInfo());
    }
}
