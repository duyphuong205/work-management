package com.cloud.work.service.impl;

import com.cloud.work.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class OtpServiceImpl implements OtpService {

    private final StringRedisTemplate redisTemplate;
}
