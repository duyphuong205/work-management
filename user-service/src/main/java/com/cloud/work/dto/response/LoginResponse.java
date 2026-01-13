package com.cloud.work.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse implements Serializable {
    String accessToken;
    long expireAccessToken;
    String refreshToken;
    long expireRefreshToken;
}
