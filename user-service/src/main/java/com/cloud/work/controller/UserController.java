package com.cloud.work.controller;

import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.request.VerifyUserRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.service.OtpService;
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
    private final OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<?> doSignup(@RequestBody UserRegisterRequest userRegisterRequest) {
        AppResponse appResponse = userInfoService.registerUser(userRegisterRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> doVerifyOtp(@RequestBody VerifyUserRequest verifyUserRequest) {
        AppResponse appResponse = otpService.verifyOtp(verifyUserRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
}
