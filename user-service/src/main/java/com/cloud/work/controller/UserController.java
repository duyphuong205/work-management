package com.cloud.work.controller;

import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.request.VerifyUserRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.facade.UserInfoFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserInfoFacade userInfoFacade;

    @GetMapping("/find-by-email")
    public ResponseEntity<?> doGetByEmail(@RequestParam String email) {
        AppResponse appResponse = userInfoFacade.getByEmail(email);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> doRegister(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        AppResponse appResponse = userInfoFacade.register(userRegisterRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> doVerifyOtp(@Valid @RequestBody VerifyUserRequest verifyUserRequest) {
        AppResponse appResponse = userInfoFacade.verifyOtp(verifyUserRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
}