package com.cloud.work.security;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.entity.UserInfo;
import com.cloud.work.exception.NotFoundException;
import com.cloud.work.repository.UserInfoRepository;
import com.cloud.work.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException(AppConstants.RES_NOT_FOUND_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_NOT_FOUND)));
        return new CustomUserDetails(userInfo);
    }
}