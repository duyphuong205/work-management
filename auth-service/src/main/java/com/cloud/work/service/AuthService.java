package com.cloud.work.service;

import com.cloud.work.dto.request.LoginRequest;
import com.cloud.work.dto.request.RefreshTokenRequest;
import com.cloud.work.dto.response.LoginResponse;

public interface AuthService {
    void logout();
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
