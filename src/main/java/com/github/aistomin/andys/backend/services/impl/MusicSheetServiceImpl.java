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
package com.github.aistomin.andys.backend.services.impl;

import com.github.aistomin.andys.backend.controllers.music.sheet.MusicSheetDto;
import com.github.aistomin.andys.backend.controllers.music.sheet.MusicSheets;
import com.github.aistomin.andys.backend.services.MusicSheetService;
import java.util.Arrays;
import org.springframework.stereotype.Service;

/**
 * Music sheets service's implementation.
 *
 * @since 0.1
 */
@Service
public final class MusicSheetServiceImpl implements MusicSheetService {

    @Override
    public MusicSheets load() {
        return new MusicSheets(
            Arrays.asList(
                new MusicSheetDto(
                    1L, "ms_1"
                ),
                new MusicSheetDto(
                    2L, "ms_2"
                )
            )
        );
    }
}
