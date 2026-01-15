package com.cloud.work.service;

import com.cloud.work.dto.request.LoginRequest;
import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.entity.UserInfo;

import java.util.Map;
import java.util.Optional;

public interface UserInfoService {
    UserInfo getUserInfoByEmail(String email);
    AppResponse registerUser(UserRegisterRequest userRegisterRequest);
    AppResponse login(LoginRequest loginRequest);
    int countUserInfoByCondition(Map<String, Object> params);
}
