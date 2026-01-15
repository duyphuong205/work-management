package com.cloud.work.service.impl;

import com.cloud.work.dto.request.VerifyUserRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.entity.UserInfo;
import com.cloud.work.enums.Status;
import com.cloud.work.repository.UserInfoRepository;
import com.cloud.work.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class OtpServiceImpl implements OtpService {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserInfoRepository userInfoRepository;

    public AppResponse verifyOtp(VerifyUserRequest verifyUserRequest) {

        String getKey = redisTemplate.opsForValue().get(verifyUserRequest.getEmail());

        boolean compareKey = getKey.equals(verifyUserRequest.getCode());
        if (compareKey){
            userInfoRepository.updateStatusByEmail(verifyUserRequest.getEmail(), Status.ACTV.name());
            redisTemplate.delete(getKey);
        } else {
            return AppResponse.builder().message("OTP in correct").build();
        }
        return AppResponse.builder().code("200").build();
    }
}
