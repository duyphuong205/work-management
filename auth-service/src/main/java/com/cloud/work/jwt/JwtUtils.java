package com.cloud.work.jwt;

import com.cloud.work.dto.response.UserInfoResponse;
import com.cloud.work.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {

    @Value("${app.jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${app.jwt.access.token.expiration}")
    private long jwtAccessTokenExpiration;

    @Value("${app.jwt.refresh.token.expiration}")
    private long jwtRefreshTokenExpiration;

    public Map<String, Object> generateToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + jwtAccessTokenExpiration);
        Date refreshExpireDate = new Date(now.getTime() + jwtRefreshTokenExpiration);

        UserInfoResponse userInfo = userDetails.getUserInfo();
        Map<String, String> claim = new HashMap<>();
        claim.put("userId", String.valueOf(userInfo.getUserId()));
        claim.put("email", userInfo.getEmail());
        claim.put("fullName", userInfo.getFullName());
        claim.put("role", userInfo.getRole());
        claim.put("status", userInfo.getStatus());

        Map<String, Object> result = new HashMap<>();
        String token = Jwts.builder()
                .claim("user", claim)
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(getSigningKey())
                .compact();

        String refreshToken = Jwts.builder()
                .claim("user", claim)
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(refreshExpireDate)
                .signWith(getSigningRefreshTokenKey())
                .compact();

        result.put("token", token);
        result.put("refreshToken", refreshToken);
        result.put("expire", expireDate.getTime());
        result.put("refreshExpire", refreshExpireDate.getTime());
        return result;
    }

    public boolean validateToken(String authToken) {
        try {
            if (StringUtils.isNotEmpty(authToken)) {
                Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
                return true;
            }
        } catch (MalformedJwtException ex) {
            log.error(">>> Error JwtTokenProvider Invalid JWT token: ", ex);
        } catch (ExpiredJwtException ex) {
            log.error(">>> Error JwtTokenProvider Expired JWT token: ", ex);
        } catch (UnsupportedJwtException ex) {
            log.error(">>> Error JwtTokenProvider Unsupported JWT token: ", ex);
        } catch (IllegalArgumentException ex) {
            log.error(">>> Error JwtTokenProvider JWT claims string is empty: ", ex);
        } catch (SecurityException ex) {
            log.error(">>> Error JwtTokenProvider JWT signature does not match locally computed signature: ", ex);
        } catch (Exception ex) {
            log.error(">>> Error JwtTokenProvider validateToken() Error: ", ex);
        }
        return false;
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            if (StringUtils.isNotEmpty(refreshToken)) {
                Jwts.parser().verifyWith(getSigningRefreshTokenKey()).build().parseSignedClaims(refreshToken);
                return true;
            }
        } catch (MalformedJwtException ex) {
            log.error(">>> Error JwtTokenProvider Invalid JWT token: ", ex);
        } catch (ExpiredJwtException ex) {
            log.error(">>> Error JwtTokenProvider Expired JWT token: ", ex);
        } catch (UnsupportedJwtException ex) {
            log.error(">>> Error JwtTokenProvider Unsupported JWT token: ", ex);
        } catch (IllegalArgumentException ex) {
            log.error(">>> Error JwtTokenProvider JWT claims string is empty: ", ex);
        } catch (SecurityException ex) {
            log.error(">>> Error JwtTokenProvider JWT signature does not match locally computed signature: ", ex);
        } catch (Exception ex) {
            log.error(">>> Error JwtTokenProvider validateRefreshToken() Error: ", ex);
        }
        return false;
    }

    public String getUserMailFromJwt(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build().parseSignedClaims(token).getPayload();
        return String.valueOf(claims.getSubject());
    }

    public String getUserMailFromRefreshToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningRefreshTokenKey())
                .build().parseSignedClaims(token).getPayload();
        return String.valueOf(claims.getSubject());
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String getTokenHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotEmpty(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    public Map<String, Object> getUserFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return (Map<String, Object>) claims.get("user");
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getSigningRefreshTokenKey() {
        byte[] keyBytes = (jwtSecretKey + "Refresh").getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build().parseSignedClaims(token).getPayload();
    }
}
