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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

/**
 * Data object that stores person's data.
 *
 * @since 0.2
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public final class Person {

    /**
     * Person ID.
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * First Name.
     */
    @Column
    private String firstName;

    /**
     * Last Name.
     */
    @Column
    private String lastName;

    /**
     * Person's email.
     */
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Does the person allow us to send newsletters?
     */
    @Column(nullable = false)
    private Boolean allowToSendNewsLetters;

    /**
     * The date when the person was created.
     */
    @Column(nullable = false)
    private Date createdOn;
}
