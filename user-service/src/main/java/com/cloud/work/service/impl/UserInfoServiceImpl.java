package com.cloud.work.service.impl;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.LoginRequest;
import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.dto.response.LoginResponse;
import com.cloud.work.dto.response.UserInfoResponse;
import com.cloud.work.entity.MessageTemplate;
import com.cloud.work.entity.SystemParameter;
import com.cloud.work.entity.UserInfo;
import com.cloud.work.enums.Role;
import com.cloud.work.enums.Status;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.jwt.JwtUtils;
import com.cloud.work.repository.UserInfoRepository;
import com.cloud.work.security.CustomUserDetails;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class UserInfoServiceImpl implements UserInfoService {

    private final JwtUtils jwtUtils;
    private final MailService mailService;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final AuthenticationManager authenticationManager;
    private final SystemParameterService systemParameterService;
    private final MessageTemplateService messageTemplateService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public UserInfo getUserInfoByEmail(String email) {
        return userInfoRepository.findByEmail(email).orElseThrow(() ->
                new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_NOT_FOUND), 404));
    }

    @Override
    public AppResponse registerUser(UserRegisterRequest userRegisterRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", Status.ACTV.name());
        params.put("email", userRegisterRequest.getEmail());
        int countUserExists = countUserInfoByCondition(params);
        if (countUserExists > 0) {
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_ALREADY_EXISTS), 409);
        }
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setRole(Role.USER);
            userInfo.setStatus(Status.SECP.name());
            userInfo.setEmail(userRegisterRequest.getEmail());
            userInfo.setFullName(userRegisterRequest.getFullName());
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
            params.put("%otpNum%", generateOtp);
            params.put("%fullName%", userInfo.getFullName());
            params.put("%otpExpireMinutes%", otpExpireMinutes);
            redisTemplate.opsForValue().set(userRegisterRequest.getEmail(), generateOtp, Duration.ofMinutes(otpExpireMinutes));

            String title = messageTemplate.getTitle();
            String content = StringUtils.messageReplace(messageTemplate.getContent(), params);
            mailService.sendEmail(userInfo.getEmail(), title, content);

            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .email(userInfo.getEmail())
                    .fullName(userInfo.getFullName())
                    .status(MessageUtils.getMessage(MessageConstants.MSG_STATUS_SECP))
                    .createdTime(DateTimeUtils.parseString(userInfo.getCreatedTime(), AppConstants.FORMAT_DATE_DD_MM_YYYY))
                    .build();

            return AppResponse.builder()
                    .code(AppConstants.RES_SUCCESS_CODE)
                    .message(MessageUtils.getMessage(MessageConstants.MSG_REGISTER_PENDING_ACTIVATION))
                    .data(userInfoResponse)
                    .build();
        } catch (Exception ex) {
            log.error(">>>UserInfoServiceImpl registerUser() ERROR", ex);
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_SYSTEM_ERROR), 500);
        }
    }

    @Override
    public AppResponse login(LoginRequest loginRequest) {
        UserInfo userInfo = getUserInfoByEmail(loginRequest.getEmail());
        if (Status.SECP.name().equals(userInfo.getStatus())) {
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_NOT_ACTIVATED), 403);
        }
        if (Status.DLTD.name().equals(userInfo.getStatus())) {
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_DELETED), 410);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

            String role = principal.getRole();
            String token = jwtUtils.generateToken(principal, role);

            LoginResponse loginResponse = LoginResponse.builder()
                    .role(role)
                    .email(loginRequest.getEmail())
                    .accessToken(token)
                    .build();

            return AppResponse.builder()
                    .code(AppConstants.RES_SUCCESS_CODE)
                    .message(MessageUtils.getMessage(MessageConstants.MSG_LOGIN_SUCCESS))
                    .data(loginResponse)
                    .build();
        } catch (Exception ex) {
            log.error(">>>UserInfoServiceImpl login() ERROR", ex);
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_SYSTEM_ERROR), 500);
        }
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