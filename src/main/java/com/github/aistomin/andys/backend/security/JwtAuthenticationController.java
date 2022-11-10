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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * JWT authentication controller.
 *
 * @since 0.1
 */
@RestController
@CrossOrigin
public final class JwtAuthenticationController {

    /**
     * Authentication manager.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * JWT utils.
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * User details service.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Authenticate user.
     *
     * @param request Authentication request.
     * @return JWT response.
     * @throws Exception If something goes wrong.
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(
        @RequestBody final JwtRequest request
    ) throws Exception {
        authenticate(request.getUsername(), request.getPassword());
        final UserDetails userDetails = this.userDetailsService
            .loadUserByUsername(request.getUsername());
        final String token = this.jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Authenticate user.
     *
     * @param username Username.
     * @param password Password.
     * @throws Exception If something goes wrong.
     */
    private void authenticate(
        final String username, final String password
    ) throws Exception {
        try {
            this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (final DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (final BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
