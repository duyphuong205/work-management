package com.cloud.work.service.impl;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.FieldConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.LoginRequest;
import com.cloud.work.dto.request.RefreshTokenRequest;
import com.cloud.work.dto.request.UserRegisterRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.dto.response.LoginResponse;
import com.cloud.work.dto.response.UserInfoResponse;
import com.cloud.work.entity.MessageTemplate;
import com.cloud.work.entity.SystemParameter;
import com.cloud.work.entity.TokenHistory;
import com.cloud.work.entity.UserInfo;
import com.cloud.work.enums.Role;
import com.cloud.work.enums.Status;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.exception.NotFoundException;
import com.cloud.work.jwt.JwtUtils;
import com.cloud.work.repository.UserInfoRepository;
import com.cloud.work.security.CustomUserDetails;
import com.cloud.work.security.CustomUserDetailsService;
import com.cloud.work.service.*;
import com.cloud.work.utils.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class UserInfoServiceImpl implements UserInfoService {

    private final JwtUtils jwtUtils;
    private final MailService mailService;
    private final HttpServletRequest request;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final TokenHistoryService tokenHistoryService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;
    private final SystemParameterService systemParameterService;
    private final MessageTemplateService messageTemplateService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public UserInfo getUserInfoByEmail(String email) {
        return userInfoRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException(AppConstants.RES_NOT_FOUND_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_NOT_FOUND)));
    }

    @Override
    public AppResponse registerUser(UserRegisterRequest userRegisterRequest) {
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

        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .email(email)
                .fullName(fullName)
                .status(MessageUtils.getMessage(MessageConstants.MSG_STATUS_SECP))
                .createdTime(DateTimeUtils.parseString(userInfo.getCreatedTime(), AppConstants.FORMAT_DATE_DD_MM_YYYY))
                .build();

        return AppResponse.builder()
                .code(AppConstants.RES_SUCCESS_CODE)
                .message(MessageUtils.getMessage(MessageConstants.MSG_REGISTER_PENDING_ACTIVATION))
                .data(userInfoResponse)
                .build();
    }

    @Override
    public AppResponse authentication(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        UserInfo userInfo = getUserInfoByEmail(email);
        if (Status.SECP.name().equals(userInfo.getStatus())) {
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_NOT_ACTIVATED));
        }
        if (Status.DLTD.name().equals(userInfo.getStatus())) {
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_DELETED));
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        LoginResponse loginResponse = this.genTokenResponse(customUserDetails);

        return AppResponse.builder()
                .code(AppConstants.RES_SUCCESS_CODE)
                .message(MessageUtils.getMessage(MessageConstants.MSG_LOGIN_SUCCESS))
                .data(loginResponse)
                .build();
    }

    @Override
    public AppResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            throw new BusinessException(AppConstants.RES_INVALID_CODE, MessageUtils.getMessage(MessageConstants.MSG_REFRESH_TOKEN_INVALID));
        }

        String email = jwtUtils.getUserMailFromRefreshToken(refreshToken);
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
        LoginResponse loginResponse = this.genTokenResponse(customUserDetails);
        return AppResponse.builder()
                .code(AppConstants.RES_SUCCESS_CODE)
                .message(MessageUtils.getMessage(MessageConstants.MSG_SUCCESS))
                .data(loginResponse)
                .build();
    }

    @Override
    public AppResponse logout() {
        Map<String, Object> params = new HashMap<>();
        String token = jwtUtils.getTokenHeader(request);
        if (jwtUtils.isTokenExpired(token)) {
            Map<String, Object> mapClaim = jwtUtils.getUserFromToken(token);
            params.put(FieldConstants.USER_ID, mapClaim.get("userId").toString());
            tokenHistoryService.expireToken(params);
            return AppResponse.builder()
                    .code(AppConstants.RES_SUCCESS_CODE)
                    .message(MessageUtils.getMessage(MessageConstants.MSG_SUCCESS))
                    .build();
        }

        Map<String, Object> mapClaim = jwtUtils.getUserFromToken(token);
        params.put(FieldConstants.USER_ID, mapClaim.get("userId").toString());
        tokenHistoryService.expireToken(params);
        return AppResponse.builder()
                .code(AppConstants.RES_SUCCESS_CODE)
                .message(MessageUtils.getMessage(MessageConstants.MSG_LOGOUT_SUCCESS))
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

    private LoginResponse genTokenResponse(CustomUserDetails customUserDetails) {
        Map<String, Object> resultMap = jwtUtils.generateToken(customUserDetails);
        String token = resultMap.get("token").toString();
        String refreshToken = resultMap.get("refreshToken").toString();
        long expire = Long.parseLong(resultMap.get("expire").toString());
        long refreshExpire = Long.parseLong(resultMap.get("refreshExpire").toString());
        long userId = customUserDetails.getUserInfo().getUserId();

        TokenHistory tokenHistory = new TokenHistory();
        tokenHistory.setUserId(userId);
        tokenHistory.setToken(token);
        tokenHistory.setRefreshToken(refreshToken);
        tokenHistory.setExpireTime(new Timestamp(expire));
        tokenHistory.setRefreshExpireTime(new Timestamp(refreshExpire));
        tokenHistory.setStatus(Status.NEWR.name());
        tokenHistoryService.insert(tokenHistory);

        return LoginResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .expireAccessToken(expire)
                .expireRefreshToken(refreshExpire)
                .build();
    }
}