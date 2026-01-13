package com.cloud.work.service;

import com.cloud.work.dao.model.TaskInfo;

import java.util.List;

public interface TaskInfoService {
    List<TaskInfo> findAll();
}
