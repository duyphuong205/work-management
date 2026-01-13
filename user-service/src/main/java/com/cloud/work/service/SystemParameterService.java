package com.cloud.work.service;

import com.cloud.work.entity.SystemParameter;

public interface SystemParameterService {
    SystemParameter getByTypeAndCode(String type, String code);
}
