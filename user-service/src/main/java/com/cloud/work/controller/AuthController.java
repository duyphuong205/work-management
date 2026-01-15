package com.cloud.work.controller;

import com.cloud.work.dto.request.LoginRequest;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserInfoService userInfoService;

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginRequest loginRequest) {
        AppResponse appResponse = userInfoService.login(loginRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
}
