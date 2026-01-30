package com.cloud.work.service;

import com.cloud.work.entity.ProjectInfo;

import java.util.List;

public interface ProjectInfoService {
    List<ProjectInfo> findAll();
    ProjectInfo create(ProjectInfo projectInfo);
    boolean existsByName(String name);
    void updateStatusById(Long id, String status);
    ProjectInfo update(ProjectInfo projectInfo);
    ProjectInfo getById(Long projectId);
}
