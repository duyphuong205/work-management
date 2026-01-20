package com.cloud.work.controller;

import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.LoginRequest;
import com.cloud.work.dto.request.RefreshTokenRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.service.AuthService;
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

    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<?> doLogin(@Valid @RequestBody LoginRequest loginRequest) {
        AppResponse appResponse = AppResponse.success(MessageConstants.MSG_LOGIN_SUCCESS, authService.login(loginRequest));
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> doRefreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        AppResponse appResponse = AppResponse.success(authService.refreshToken(refreshTokenRequest));
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> doLogout() {
        authService.logout();
        return new ResponseEntity<>(AppResponse.success(), HttpStatus.OK);
    }
}