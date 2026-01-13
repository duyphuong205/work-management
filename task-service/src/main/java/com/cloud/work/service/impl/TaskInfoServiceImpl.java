package com.cloud.work.service.impl;

import com.cloud.work.dao.mapper.TaskInfoMapper;
import com.cloud.work.dao.model.TaskInfo;
import com.cloud.work.service.TaskInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskInfoServiceImpl implements TaskInfoService {

    private final TaskInfoMapper taskInfoMapper;

    @Override
    public List<TaskInfo> findAll() {
        return taskInfoMapper.findAll();
    }
}
