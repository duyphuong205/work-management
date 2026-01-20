package com.cloud.work.controller;

import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.request.VerifyUserRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.dto.response.UserInfoResponse;
import com.cloud.work.service.OtpService;
import com.cloud.work.service.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final OtpService otpService;
    private final UserInfoService userInfoService;

    @GetMapping("/find-by-email")
    public ResponseEntity<?> doGetByEmail(@RequestParam String email) {
        AppResponse appResponse = AppResponse.success(userInfoService.getUserInfoByEmail(email));
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> doRegister(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        UserInfoResponse userInfoResponse = userInfoService.registerUser(userRegisterRequest);
        AppResponse appResponse = AppResponse.success(MessageConstants.MSG_REGISTER_PENDING_ACTIVATION, userInfoResponse);
        return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> doVerifyOtp(@Valid @RequestBody VerifyUserRequest verifyUserRequest) {
        AppResponse appResponse = otpService.verifyOtp(verifyUserRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
}