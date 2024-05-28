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
package com.github.aistomin.andys.backend;

import com.github.aistomin.andys.backend.controllers.user.RegistrationDto;
import com.github.aistomin.andys.backend.controllers.video.VideoDto;
import com.github.aistomin.andys.backend.services.UserService;
import com.github.aistomin.andys.backend.services.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;

/**
 * Application class.
 *
 * @since 0.1
 */
@SpringBootApplication
public class Application {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * User service.
     */
    private final UserService users;

    /**
     * Video service.
     */
    private final VideoService videos;

    /**
     * Ctor.
     *
     * @param userService  User service.
     * @param videoService Video service.
     */
    public Application(
        final UserService userService,
        final VideoService videoService
    ) {
        this.users = userService;
        this.videos = videoService;
    }

    /**
     * App entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Command line runner.
     *
     * @param ctx Application context.
     * @return Runner.
     */
    @Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return args -> {
            logger.info("Application is starting .....");
            final String username = "admin";
            final boolean adminUserExists = this.users
                .loadAll()
                .getContent()
                .stream()
                .anyMatch(user -> username.equals(user.getUsername()));
            if (!adminUserExists) {
                this.createUser(username);
                this.generateDemoVideos();
            }
            logger.info("Application is up and running.");
        };
    }

    /**
     * Create a user with username.
     *
     * @param username Username.
     */
    private void createUser(final String username) {
        this.logger.info("Admin user is missing. Let's create it .....");
        final var admin = new RegistrationDto();
        admin.setUsername(username);
        admin.setPassword(username);
        this.users.register(admin);
        this.logger.info("Admin user is created.");
    }

    /**
     * Generate demo videos.
     */
    @SuppressWarnings({
        "checkstyle:MagicNumber",
        "checkstyle:LineLength",
        "checkstyle:MethodLength"
    })
    private void generateDemoVideos() {
        this.logger.info("Generate demo videos .....");
        this.videos.save(new VideoDto(
            null,
            "Matteo Carcassi - Prelude in E Minor // Andrej Istomin",
            "Matteo Carcassi(1792 – 1853): \"Prelude in E Minor\". Performed by Andrej Istomin.",
            "https://www.youtube.com/watch?v=FtJ69YrA3Ok",
            "FtJ69YrA3Ok",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2017, 8, 20)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2017, 8, 20)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.videos.save(new VideoDto(
            null,
            "Klaus Schindler - Zeit für Träume // Andrej Istomin",
            "The photos in this video were taken by my wife(checkout her channel, by the way:  https://www.youtube.com/channel/UC810...) in Landkreis Dachau(mostly in Schwabhausen and Altomünster). The lovely place where I live.",
            "https://www.youtube.com/watch?v=lLe-HB1WLO4",
            "lLe-HB1WLO4",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2017, 8, 20)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2017, 8, 20)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.videos.save(new VideoDto(
            null,
            "Andrej Istomin - Jenny",
            "The melody composed and performed by Andrej Istomin except those telephone sounds.",
            "https://www.youtube.com/watch?v=j_Ve3uiVsaA",
            "j_Ve3uiVsaA",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2017, 8, 21)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2017, 8, 21)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.videos.save(new VideoDto(
            null,
            "Mauro Giuliani - Sonatina op. 71 no. 1 // Andrej Istomin",
            "A little bit of an autumn mood :) Music piece is composed by Mauro Giuliani, called \"Sonatina op71 n°1\". The pictures in slide-show are the reproductions of different Russian painters.",
            "https://www.youtube.com/watch?v=DIEBQxqY1yU",
            "DIEBQxqY1yU",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2017, 11, 11)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2017, 11, 11)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.videos.save(new VideoDto(
            null,
            "Blackmore's Night - Minstrel Hall // Andrej Istomin",
            "The piece is composed by Ritchie Blackmore.",
            "https://www.youtube.com/watch?v=iTVHH1vMIcI",
            "iTVHH1vMIcI",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2017, 11, 13)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2017, 11, 13)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.videos.save(new VideoDto(
            null,
            "Fernando Sor - Andante // Andrej Istomin",
            "This time I'm playing \"Andante\" by Fernando Sor. It is the very first time when I try to create a real video instead of slide show which I did for my previous videos. I hope you like it :)",
            "https://www.youtube.com/watch?v=kbd7a7j0VOE",
            "kbd7a7j0VOE",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2017, 12, 15)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2017, 12, 15)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.videos.save(new VideoDto(
            null,
            "Greensleeves(English traditional) // Andrej Istomin",
            "Today I'd like to show you my attempt to play instrumental version of the traditional English song \"Greensleeves\".",
            "https://www.youtube.com/watch?v=DDt7N5KxXrM",
            "DDt7N5KxXrM",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2019, 5, 30)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2019, 5, 30)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.videos.save(new VideoDto(
            null,
            "Ferdinando Carulli - Andantino // Andrej Istomin",
            "This summer is really hot here in Germany. I decided to create this small summer video where I'm trying to play nice light piece called \"Andantino\" by Ferdinando Carulli (1770-1841).",
            "https://www.youtube.com/watch?v=Hd05dNHYqAw",
            "Hd05dNHYqAw",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2019, 7, 9)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2019, 7, 9)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.videos.save(new VideoDto(
            null,
            "Johann Pachelbel - Sarabande (Guitar + Recorder) // Andrej Istomin",
            "This summer is really hot here in Germany. I decided to create this small summer video where I'm trying to play nice light piece called \"Andantino\" by Ferdinando Carulli (1770-1841).",
            "https://www.youtube.com/watch?v=hRvXB_gnR84",
            "hRvXB_gnR84",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2020, 6, 13)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2020, 6, 13)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.videos.save(new VideoDto(
            null,
            "J. S. Bach - Bourrée // Andrej Istomin",
            "Thanks for watching this video. If you like it, please subscribe to my channel, comment the videos, \"like\" them and share with your friends.",
            "https://www.youtube.com/watch?v=AjQBAQ1jzwc",
            "AjQBAQ1jzwc",
            new HashSet<>(),
            Date.from(
                LocalDate.of(2021, 4, 9)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            ),
            Date.from(
                LocalDate.of(2021, 4, 9)
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC)
            )
        ));
        this.logger.info("Demo videos were successfully generated.");
    }
}
