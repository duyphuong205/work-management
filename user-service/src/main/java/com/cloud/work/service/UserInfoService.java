package com.cloud.work.service;

import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.request.VerifyUserRequest;
import com.cloud.work.dto.response.AppResponse;

public interface UserInfoService {
    boolean getUserInfoByEmail(String email);
    AppResponse registerUser(UserRegisterRequest userRegisterRequest);

}
