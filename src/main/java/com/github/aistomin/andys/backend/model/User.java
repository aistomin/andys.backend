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

import com.github.aistomin.andys.backend.controllers.user.RegistrationDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;

/**
 * Data object that stores user's data.
 *
 * @since 0.1
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
@Entity
@Table(name = "andys_user")
public final class User {

    /**
     * User ID.
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Username.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The date when the user was created.
     */
    @Column(nullable = false)
    private Date createdOn;

    /**
     * Ctor.
     *
     * @param user User DTO.
     */
    public User(final RegistrationDto user) {
        this(null, user.getUsername(), user.getPassword(), new Date());
    }
}
