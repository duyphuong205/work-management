package com.cloud.work.facade;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.request.VerifyUserRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.dto.response.UserInfoResponse;
import com.cloud.work.entity.MessageTemplate;
import com.cloud.work.entity.SystemParameter;
import com.cloud.work.entity.UserInfo;
import com.cloud.work.enums.Role;
import com.cloud.work.enums.Status;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.service.MailService;
import com.cloud.work.service.MessageTemplateService;
import com.cloud.work.service.SystemParameterService;
import com.cloud.work.service.UserInfoService;
import com.cloud.work.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoFacade {

    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoService userInfoService;
    private final RedisTemplate<String, String> redisTemplate;
    private final SystemParameterService systemParameterService;
    private final MessageTemplateService messageTemplateService;

    public AppResponse register(UserRegisterRequest userRegisterRequest) {
        UserInfo userInfo = new UserInfo();

        String email = userRegisterRequest.getEmail();
        String fullName = userRegisterRequest.getFullName();

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("status", Status.ACTV.name());
        int countUserExists = userInfoService.countByCondition(params);
        if (countUserExists > 0) {
            throw new BusinessException(AppConstants.RES_DUPLICATE_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_ALREADY_EXISTS));
        }
        userInfo.setEmail(email);
        userInfo.setRole(Role.USER);
        userInfo.setFullName(fullName);
        userInfo.setStatus(Status.SECP.name());
        userInfo.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        userInfoService.create(userInfo);

        SystemParameter systemParameter = systemParameterService.getByTypeAndCode("OTP_TIME_OUT", "OTP");
        MessageTemplate messageTemplate = messageTemplateService.getByTypeAndLanguage("UG001", LanguageUtils.getCurrentLanguage());

        long otpExpireMinutes = 0;
        if (Objects.nonNull(systemParameter)) {
            otpExpireMinutes = Long.parseLong(systemParameter.getValue());
        }

        params.clear();
        String generateOtp = OtpUtils.generateOtp();
        params.put("%fullName%", fullName);
        params.put("%otpNum%", generateOtp);
        params.put("%otpExpireMinutes%", otpExpireMinutes);
        redisTemplate.opsForValue().set(email, generateOtp, Duration.ofMinutes(otpExpireMinutes));

        String title = messageTemplate.getTitle();
        String content = StringUtils.messageReplace(messageTemplate.getContent(), params);
        mailService.sendEmail(email, title, content);

        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .email(email)
                .fullName(fullName)
                .status(MessageUtils.getMessage(MessageConstants.MSG_STATUS_SECP))
                .createdTime(DateTimeUtils.parseString(userInfo.getCreatedTime(), AppConstants.FORMAT_DATE_DD_MM_YYYY))
                .build();
        return AppResponse.success(MessageConstants.MSG_REGISTER_PENDING_ACTIVATION, userInfoResponse);
    }

    public AppResponse verifyOtp(VerifyUserRequest verifyUserRequest) {
        String email = verifyUserRequest.getEmail();
        String otpValue = redisTemplate.opsForValue().get(email);
        if (org.apache.commons.lang3.StringUtils.isEmpty(otpValue)) {
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_VERIFY_OTP_EXPIRED));
        }
        if (!otpValue.equals(verifyUserRequest.getOtp())) {
            throw new BusinessException(AppConstants.RES_INVALID_CODE, MessageUtils.getMessage(MessageConstants.MSG_VERIFY_OTP_INVALID));
        }
        userInfoService.updateStatusByEmail(email, Status.ACTV.name());
        redisTemplate.delete(otpValue);

        return AppResponse.builder()
                .code(AppConstants.RES_SUCCESS_CODE)
                .message(MessageUtils.getMessage(MessageConstants.MSG_VERIFY_ACCOUNT_SUCCESS))
                .build();
    }

    public AppResponse getByEmail(String email) {
        UserInfo userInfo = userInfoService.getByEmail(email);
        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .email(email)
                .userId(userInfo.getUserId())
                .status(userInfo.getStatus())
                .role(userInfo.getRole().name())
                .fullName(userInfo.getFullName())
                .password(userInfo.getPassword())
                .createdTime(DateTimeUtils.parseString(userInfo.getCreatedTime(), AppConstants.FORMAT_DATE_DD_MM_YYYY))
                .build();
        return AppResponse.success(userInfoResponse);
    }
}
