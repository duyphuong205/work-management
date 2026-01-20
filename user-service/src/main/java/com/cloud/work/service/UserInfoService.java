package com.cloud.work.service;

import com.cloud.work.entity.UserInfo;

import java.util.Map;

public interface UserInfoService {
    UserInfo create(UserInfo userInfo);
    void updateStatusByEmail(String email, String status);
    UserInfo getByEmail(String email);
    int countByCondition(Map<String, Object> params);
}
