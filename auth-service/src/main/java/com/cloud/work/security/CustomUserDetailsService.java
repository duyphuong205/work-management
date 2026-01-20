package com.cloud.work.security;

import com.cloud.work.client.UserServiceClient;
import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.dto.response.UserInfoResponse;
import com.cloud.work.exception.NotFoundException;
import com.cloud.work.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppResponse appResponse = userServiceClient.getUserByEmail(email);
        UserInfoResponse userInfo = objectMapper.convertValue(appResponse.getData(), UserInfoResponse.class);
        if (Objects.isNull(userInfo)) {
            throw new NotFoundException(AppConstants.RES_NOT_FOUND_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_NOT_FOUND));
        }
        return new CustomUserDetails(userInfo);
    }
}