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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

/**
 * Data object that stores emails that we send.
 *
 * @since 0.2
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public final class EmailMessage {

    /**
     * Length of email body.
     */
    private static final int BODY_LENGTH = 100_000;

    /**
     * Email message ID.
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Email dispatcher.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Person dispatcher;

    /**
     * Email receptor.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Person receptor;

    /**
     * Email subject.
     */
    @Column(nullable = false)
    private String subject;

    /**
     * Email body.
     */
    @Column(nullable = false, length = BODY_LENGTH)
    private String body;

    /**
     * Email message status.
     */
    @Column(nullable = false)
    private EmailMessageStatus status = EmailMessageStatus.CREATED;

    /**
     * Email message type.
     */
    @Column(nullable = false)
    private EmailMessageType type;

    /**
     * Some additional free text info about the email message.
     */
    @Column(length = BODY_LENGTH)
    private String info;

    /**
     * The date when the email message was created.
     */
    @Column(nullable = false)
    private Date createdOn;
}
