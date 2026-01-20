package com.cloud.work.service.impl;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.response.UserInfoResponse;
import com.cloud.work.entity.MessageTemplate;
import com.cloud.work.entity.SystemParameter;
import com.cloud.work.entity.UserInfo;
import com.cloud.work.enums.Role;
import com.cloud.work.enums.Status;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.exception.NotFoundException;
import com.cloud.work.repository.UserInfoRepository;
import com.cloud.work.service.MailService;
import com.cloud.work.service.MessageTemplateService;
import com.cloud.work.service.SystemParameterService;
import com.cloud.work.service.UserInfoService;
import com.cloud.work.utils.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class UserInfoServiceImpl implements UserInfoService {

    private final MailService mailService;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final SystemParameterService systemParameterService;
    private final MessageTemplateService messageTemplateService;

    @Override
    public UserInfoResponse getUserInfoByEmail(String email) {
        UserInfo userInfo = userInfoRepository.findByEmail(email);
        if (Objects.isNull(userInfo)) {
            throw new NotFoundException(AppConstants.RES_NOT_FOUND_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_NOT_FOUND));
        }
        return UserInfoResponse.builder()
                .email(email)
                .userId(userInfo.getUserId())
                .status(userInfo.getStatus())
                .role(userInfo.getRole().name())
                .fullName(userInfo.getFullName())
                .password(userInfo.getPassword())
                .createdTime(DateTimeUtils.parseString(userInfo.getCreatedTime(), AppConstants.FORMAT_DATE_DD_MM_YYYY))
                .build();
    }

    @Override
    public UserInfoResponse registerUser(UserRegisterRequest userRegisterRequest) {
        String email = userRegisterRequest.getEmail();
        String fullName = userRegisterRequest.getFullName();

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("status", Status.ACTV.name());
        int countUserExists = countUserInfoByCondition(params);
        if (countUserExists > 0) {
            throw new BusinessException(AppConstants.RES_DUPLICATE_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_ALREADY_EXISTS));
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setRole(Role.USER);
        userInfo.setFullName(fullName);
        userInfo.setStatus(Status.SECP.name());
        userInfo.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        userInfoRepository.save(userInfo);

        long otpExpireMinutes = 0;
        SystemParameter systemParameter = systemParameterService.getByTypeAndCode("OTP_TIME_OUT", "OTP");
        MessageTemplate messageTemplate = messageTemplateService.getByTypeAndLanguage("UG001", LanguageUtils.getCurrentLanguage());
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

        return UserInfoResponse.builder()
                .email(email)
                .fullName(fullName)
                .status(MessageUtils.getMessage(MessageConstants.MSG_STATUS_SECP))
                .createdTime(DateTimeUtils.parseString(userInfo.getCreatedTime(), AppConstants.FORMAT_DATE_DD_MM_YYYY))
                .build();
    }

    @Override
    public int countUserInfoByCondition(Map<String, Object> params) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<UserInfo> root = cq.from(UserInfo.class);

        List<Predicate> ps = new ArrayList<>();
        if (params.get("email") != null) {
            ps.add(cb.equal(root.get("email"), params.get("email")));
        }
        if (params.get("status") != null) {
            ps.add(cb.equal(root.get("status"), params.get("status")));
        }

        cq.select(cb.count(root)).where(ps.toArray(new Predicate[0]));
        Long count = entityManager.createQuery(cq).getSingleResult();
        return Math.toIntExact(count);
    }
}