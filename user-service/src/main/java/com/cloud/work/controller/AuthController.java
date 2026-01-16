package com.cloud.work.controller;

import com.cloud.work.dto.request.LoginRequest;
import com.cloud.work.dto.request.LogoutRequest;
import com.cloud.work.dto.request.RefreshTokenRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.service.UserInfoService;
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

    private final UserInfoService userInfoService;

    @PostMapping("/auth")
    public ResponseEntity<?> doLogin(@RequestBody LoginRequest loginRequest) {
        AppResponse appResponse = userInfoService.authentication(loginRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> doRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AppResponse appResponse = userInfoService.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> doLogout(@RequestBody LogoutRequest logoutRequest) {
        AppResponse appResponse = userInfoService.logout(logoutRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
}
