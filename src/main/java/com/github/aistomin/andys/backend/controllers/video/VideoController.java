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
package com.github.aistomin.andys.backend.controllers.video;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Video controller.
 *
 * @since 0.1
 */
@RestController
@RequestMapping("/videos")
public final class VideoController {

    /**
     * Just 10.
     */
    private static final int TEN = 10;

    /**
     * Temporal "in memory" storage.
     */
    private final List<VideoDto> storage = new ArrayList<>();

    /**
     * Ctor.
     */
    public VideoController() {
        final var random = new Random();
        for (long i = 0; i < TEN; i++) {
            final var count = random.nextInt(TEN);
            final var tags = new ArrayList<String>(count);
            for (int j = 0; j < count; j++) {
                tags.add(UUID.randomUUID().toString());
            }
            this.storage.add(
                new VideoDto(
                    i + 1,
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    String.format(
                        "https://%s.com/%s",
                        UUID.randomUUID(), UUID.randomUUID()
                    ),
                    tags
                )
            );
        }
    }

    /**
     * Load videos.
     *
     * @return Videos.
     */
    @GetMapping
    public Videos load() {
        return new Videos(this.storage);
    }
}
