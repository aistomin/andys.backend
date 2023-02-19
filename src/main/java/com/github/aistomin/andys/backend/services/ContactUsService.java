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
package com.github.aistomin.andys.backend.services;

/**
 * "Contact Us" service.
 *
 * @since 0.2
 */
public interface ContactUsService {

    /**
     * Send us a contact request.
     *
     * @param email                  User's email.
     * @param subject                Contact request's subject.
     * @param body                   Contact request's body.
     * @param allowToSendNewsLetters Does the person allow us to send
     *                               newsletters?
     */
    void contactUs(
        String email, String subject, String body,
        Boolean allowToSendNewsLetters
    );
}
