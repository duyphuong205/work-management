package com.cloud.work.service;

import com.cloud.work.dto.request.VerifyUserRequest;
import com.cloud.work.dto.response.AppResponse;

public interface OtpService {
    AppResponse verifyOtp(VerifyUserRequest verifyUserRequest);
}
