package com.cloud.work.controller;

import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserInfoService userInfoService;

    @PostMapping("/register")
    public ResponseEntity<?> doSignup(@RequestBody UserRegisterRequest userRegisterRequest) {
        AppResponse appResponse = userInfoService.registerUser(userRegisterRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
    }
}
