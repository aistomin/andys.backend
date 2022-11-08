/*
 * Copyright (c) 2021-2022, Istomin Andrei
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
package com.github.aistomin.andys.backend.security;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JWT request.
 *
 * @since 0.1
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class JwtRequest implements Serializable {

    /**
     * Serialization-related field.
     */
    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    /**
     * Username.
     */
    private String username;

    /**
     * Password.
     */
    private String password;
}
