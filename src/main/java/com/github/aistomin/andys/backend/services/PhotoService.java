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

import com.github.aistomin.andys.backend.controllers.photo.PhotoDto;
import com.github.aistomin.andys.backend.controllers.photo.Photos;

/**
 * Photo's service.
 *
 * @since 0.1
 */
public interface PhotoService {

    /**
     * Load photos.
     *
     * @return Photos.
     */
    Photos load();

    /**
     * Save a photo.
     *
     * @param photo Photo that needs to be created.
     * @return Created photo.
     */
    PhotoDto save(PhotoDto photo);

    /**
     * Delete photo.
     *
     * @param id Photo ID.
     */
    void delete(Long id);
}
