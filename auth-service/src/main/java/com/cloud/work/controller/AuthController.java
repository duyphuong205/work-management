package com.cloud.work.controller;

import com.cloud.work.dto.request.LoginRequest;
import com.cloud.work.dto.request.RefreshTokenRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.facade.AuthFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/auth")
    public ResponseEntity<?> doLogin(@Valid @RequestBody LoginRequest loginRequest) {
        AppResponse appResponse = authFacade.login(loginRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> doRefreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        AppResponse appResponse = authFacade.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> doLogout() {
        AppResponse appResponse = authFacade.logout();
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
}