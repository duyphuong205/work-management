package com.cloud.work.service;

import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.response.UserInfoResponse;

import java.util.Map;

public interface UserInfoService {
    UserInfoResponse getUserInfoByEmail(String email);
    UserInfoResponse registerUser(UserRegisterRequest userRegisterRequest);
    int countUserInfoByCondition(Map<String, Object> params);
}
