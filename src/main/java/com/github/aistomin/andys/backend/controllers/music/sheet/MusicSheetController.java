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
package com.github.aistomin.andys.backend.controllers.music.sheet;

import com.github.aistomin.andys.backend.services.MusicSheetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Music sheets' controller.
 *
 * @since 0.1
 */
@RestController
@RequestMapping("/music/sheets")
public final class MusicSheetController {

    /**
     * Music sheets' service.
     */
    private final MusicSheetService musicSheets;

    /**
     * Ctor.
     *
     * @param service Music sheets' service.
     */
    public MusicSheetController(final MusicSheetService service) {
        this.musicSheets = service;
    }

    /**
     * Load music sheets.
     *
     * @return Music sheets.
     */
    @GetMapping
    public MusicSheets load() {
        return this.musicSheets.load();
    }

    /**
     * Create a music sheet.
     *
     * @param sheet Music sheet that needs to be created.
     * @return Created sheet.
     */
    @PostMapping()
    public ResponseEntity<MusicSheetDto> create(
        @RequestBody final MusicSheetDto sheet
    ) {
        return new ResponseEntity<>(
            this.musicSheets.save(sheet), HttpStatus.CREATED
        );
    }

    /**
     * Delete a music sheet.
     *
     * @param id Music sheet ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") final Long id) {
        this.musicSheets.delete(id);
    }
}
