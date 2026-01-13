package com.cloud.work.service;

import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.entity.UserInfo;

public interface UserInfoService {
    UserInfo getUserInfoByEmail(String email);
    AppResponse registerUser(UserRegisterRequest userRegisterRequest);
}
