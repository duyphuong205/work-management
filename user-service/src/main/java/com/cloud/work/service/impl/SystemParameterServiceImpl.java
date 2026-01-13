package com.cloud.work.service.impl;

import com.cloud.work.entity.SystemParameter;
import com.cloud.work.repository.SystemParameterRepository;
import com.cloud.work.service.SystemParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemParameterServiceImpl implements SystemParameterService {

    private final SystemParameterRepository systemParameterRepository;


    @Override
    public SystemParameter getByTypeAndCode(String type, String code) {
        return systemParameterRepository.findByTypeAndCode(type, code);
    }
}
