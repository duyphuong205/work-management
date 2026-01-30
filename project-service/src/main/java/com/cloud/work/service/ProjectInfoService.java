package com.cloud.work.service;

import com.cloud.work.dto.request.CreateProjectRequest;
import com.cloud.work.entity.ProjectInfo;

import java.util.List;

public interface ProjectInfoService {
    List<ProjectInfo> findAll();
    ProjectInfo createNewProject(ProjectInfo projectInfo);
    boolean existsByName(String name);
    void updateStatusById(Long id, String status);
    void updateNameBoard(Long id, CreateProjectRequest createProjectRequest);
}
