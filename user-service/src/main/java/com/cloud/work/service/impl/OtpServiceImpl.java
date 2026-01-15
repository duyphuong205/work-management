package com.cloud.work.service.impl;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.VerifyUserRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.enums.Status;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.repository.UserInfoRepository;
import com.cloud.work.service.OtpService;
import com.cloud.work.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class OtpServiceImpl implements OtpService {

    private final UserInfoRepository userInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public AppResponse verifyOtp(VerifyUserRequest verifyUserRequest) {
        try {
            String otpValue = redisTemplate.opsForValue().get(verifyUserRequest.getEmail());
            if (StringUtils.isEmpty(otpValue)) {
                throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_VERIFY_OTP_EXPIRED), 410);
            }
            boolean check = otpValue.equals(verifyUserRequest.getOtp());
            if (check) {
                userInfoRepository.updateStatusByEmail(verifyUserRequest.getEmail(), Status.ACTV.name());
                redisTemplate.delete(otpValue);
            }
            return AppResponse.builder()
                    .code(AppConstants.RES_SUCCESS_CODE)
                    .message(MessageUtils.getMessage(MessageConstants.MSG_LOGIN_SUCCESS))
                    .build();
        } catch (Exception ex) {
            log.error(">>>OtpServiceImpl verifyOtp() ERROR", ex);
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_SYSTEM_ERROR), 500);
        }
    }
}
