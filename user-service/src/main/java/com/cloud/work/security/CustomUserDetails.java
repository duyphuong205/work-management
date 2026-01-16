package com.cloud.work.security;

import com.cloud.work.entity.UserInfo;
import com.cloud.work.enums.Status;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class CustomUserDetails implements UserDetails {

    private final UserInfo userInfo;

    public CustomUserDetails(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userInfo.getRole().name()));
    }

    @Override
    public String getUsername() {
        return userInfo.getEmail();
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Status.ACTV.name().equals(userInfo.getStatus());
    }
}
