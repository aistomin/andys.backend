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
package com.github.aistomin.andys.backend;

import com.github.aistomin.andys.backend.controllers.user.UserDto;
import com.github.aistomin.andys.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Application class.
 *
 * @since 0.1
 */
@SpringBootApplication
public class Application {

    /**
     * User service.
     */
    @Autowired
    private UserService users;

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
            System.out.println("Application is starting .....");
            final boolean adminUserExists = this.users
                .loadAll()
                .getContent()
                .stream()
                .anyMatch(user -> "admin".equals(user.getUsername()));
            if (!adminUserExists) {
                System.out.println("Admin user is missing. Let's create it.");
                final UserDto admin = new UserDto();
                admin.setUsername("admin");
                this.users.create(admin);
            }
        };
    }
}
