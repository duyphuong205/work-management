package com.cloud.work.service.impl;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.request.VerifyUserRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.dto.response.UserInfoResponse;
import com.cloud.work.entity.MessageTemplate;
import com.cloud.work.entity.SystemParameter;
import com.cloud.work.entity.UserInfo;
import com.cloud.work.enums.Role;
import com.cloud.work.enums.Status;
import com.cloud.work.repository.UserInfoRepository;
import com.cloud.work.service.MailService;
import com.cloud.work.service.MessageTemplateService;
import com.cloud.work.service.SystemParameterService;
import com.cloud.work.service.UserInfoService;
import com.cloud.work.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class UserInfoServiceImpl implements UserInfoService {

    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final SystemParameterService systemParameterService;
    private final MessageTemplateService messageTemplateService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration OTP_TTL = Duration.ofMinutes(2);
    @Override
    public boolean getUserInfoByEmail(String email) {
        return userInfoRepository.existsByEmail(email);
    }

    @Override
    public AppResponse registerUser(UserRegisterRequest userRegisterRequest) {
        try {

            UserInfo userInfo = new UserInfo();
            String checkEmail = userRegisterRequest.getEmail();
            if (userInfoRepository.existsByEmail(checkEmail)){
                return AppResponse.builder()
                        .code(AppConstants.RES_FAIL_CODE)
                        .message(MessageUtils.getMessage("account existed"))
                        .build();
            }
            userInfo.setRole(Role.USER.name());
            userInfo.setStatus(Status.SECP.name());
            userInfo.setEmail(userRegisterRequest.getEmail());
            userInfo.setFullName(userRegisterRequest.getFullName());
            userInfo.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
            userInfoRepository.save(userInfo);


            SystemParameter systemParameter = systemParameterService.getByTypeAndCode("OTP_TIME_OUT", "OTP");
            MessageTemplate messageTemplate = messageTemplateService.getByTypeAndLanguage("UG001", LanguageUtils.getCurrentLanguage());

            Map<String, String> params = new HashMap<>();
            String generateOtp = OtpUtils.generateOtp();
            params.put("%otpNum%", generateOtp);
            params.put("%fullName%", userInfo.getFullName());
            params.put("%otpExpireMinutes%", systemParameter.getValue());
            // save otp redis and save time
            redisTemplate.opsForValue().set(userRegisterRequest.getEmail(), generateOtp, OTP_TTL);

            String title = messageTemplate.getTitle();
            String content = StringUtils.messageReplace(messageTemplate.getContent(), params);
            mailService.sendEmail(userInfo.getEmail(), title, content);

            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .fullName(userInfo.getFullName())
                    .email(userInfo.getEmail())
                    .createdTime(DateTimeUtils.parseString(userInfo.getCreatedTime(), AppConstants.FORMAT_DATE_DD_MM_YYYY))
                    .build();

            return AppResponse.builder()
                    .code(AppConstants.RES_SUCCESS_CODE)
                    .message(MessageUtils.getMessage("info.success"))
                    .data(userInfoResponse)
                    .build();
        } catch (Exception ex) {
            log.error(">>>UserInfoServiceImpl registerUser() ERROR", ex);
        }
        return null;
    }



}
