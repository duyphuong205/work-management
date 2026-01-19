package com.cloud.work.service;

import com.cloud.work.dto.request.LoginRequest;
import com.cloud.work.dto.request.RefreshTokenRequest;
import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.entity.UserInfo;

import java.util.Map;

public interface UserInfoService {
    UserInfo getUserInfoByEmail(String email);
    AppResponse registerUser(UserRegisterRequest userRegisterRequest);
    AppResponse authentication(LoginRequest loginRequest);
    AppResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    AppResponse logout();
    int countUserInfoByCondition(Map<String, Object> params);
}
