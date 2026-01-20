package com.cloud.work.service.impl;

import com.cloud.work.client.UserServiceClient;
import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.FieldConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.LoginRequest;
import com.cloud.work.dto.request.RefreshTokenRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.dto.response.LoginResponse;
import com.cloud.work.dto.response.UserInfoResponse;
import com.cloud.work.entity.TokenHistory;
import com.cloud.work.enums.Status;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.jwt.JwtUtils;
import com.cloud.work.security.CustomUserDetails;
import com.cloud.work.security.CustomUserDetailsService;
import com.cloud.work.service.AuthService;
import com.cloud.work.service.TokenHistoryService;
import com.cloud.work.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;
    private final TokenHistoryService tokenHistoryService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void logout() {
        Map<String, Object> params = new HashMap<>();
        String token = jwtUtils.getTokenHeader(request);
        if (jwtUtils.isTokenExpired(token)) {
            Map<String, Object> mapClaim = jwtUtils.getUserFromToken(token);
            params.put(FieldConstants.USER_ID, mapClaim.get("userId").toString());
            tokenHistoryService.expireToken(params);
        }
        Map<String, Object> mapClaim = jwtUtils.getUserFromToken(token);
        params.put(FieldConstants.USER_ID, mapClaim.get("userId").toString());
        tokenHistoryService.expireToken(params);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        AppResponse appResponse = userServiceClient.getUserByEmail(email);
        UserInfoResponse userInfoResponse = objectMapper.convertValue(appResponse.getData(), UserInfoResponse.class);
        if (Status.SECP.name().equals(userInfoResponse.getStatus())) {
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_NOT_ACTIVATED));
        }
        if (Status.DLTD.name().equals(userInfoResponse.getStatus())) {
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_DELETED));
        }
        if (password.equals(userInfoResponse.getPassword())) {
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_DELETED));
        }
        if (!passwordEncoder.matches(password, userInfoResponse.getPassword())) {
            throw new BusinessException(AppConstants.RES_INVALID_CODE, MessageUtils.getMessage(MessageConstants.MSG_PASSWORD_INCORRECT));
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return this.genTokenResponse(customUserDetails);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            throw new BusinessException(AppConstants.RES_INVALID_CODE, MessageUtils.getMessage(MessageConstants.MSG_REFRESH_TOKEN_INVALID));
        }
        String email = jwtUtils.getUserMailFromRefreshToken(refreshToken);
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
        return this.genTokenResponse(customUserDetails);
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
