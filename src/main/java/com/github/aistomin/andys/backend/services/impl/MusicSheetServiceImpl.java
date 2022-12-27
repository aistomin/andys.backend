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

import com.github.aistomin.andys.backend.controllers.exceptions.NotFound;
import com.github.aistomin.andys.backend.controllers.music.sheet.MusicSheetDto;
import com.github.aistomin.andys.backend.controllers.music.sheet.MusicSheets;
import com.github.aistomin.andys.backend.model.MusicSheet;
import com.github.aistomin.andys.backend.model.MusicSheetRepository;
import com.github.aistomin.andys.backend.services.MusicSheetService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Music sheets service's implementation.
 *
 * @since 0.1
 */
@Service
public final class MusicSheetServiceImpl implements MusicSheetService {

    /**
     * Music sheet repository.
     */
    private final MusicSheetRepository repo;

    /**
     * Ctor.
     *
     * @param repository Music sheet repository.
     */
    public MusicSheetServiceImpl(final MusicSheetRepository repository) {
        this.repo = repository;
    }

    @Override
    public MusicSheets load() {
        return new MusicSheets(
            this.repo.findAll().stream().map(MusicSheetDto::new).toList()
        );
    }

    @Override
    public MusicSheetDto save(final MusicSheetDto sheet) {
        return new MusicSheetDto(this.repo.save(new MusicSheet(sheet)));
    }

    @Override
    public void delete(final Long id) {
        this.repo.delete(this.findById(id));
    }

    /**
     * Find music sheet by ID.
     *
     * @param id Music sheet ID.
     * @return Music sheet.
     */
    private MusicSheet findById(final Long id) {
        final Optional<MusicSheet> found = this.repo.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFound(
                String.format("Music sheet with ID = %d not found.", id)
            );
        }
    }
}
