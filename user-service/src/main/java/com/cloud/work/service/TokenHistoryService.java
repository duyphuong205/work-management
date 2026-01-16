package com.cloud.work.service;

import com.cloud.work.entity.TokenHistory;

import java.util.Map;

public interface TokenHistoryService {
    void insert(TokenHistory tokenHistory);
    void expireToken(Map<String, Object> params);
    boolean isValidToken(String token);
}
