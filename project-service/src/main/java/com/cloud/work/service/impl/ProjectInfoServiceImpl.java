package com.cloud.work.service.impl;

import com.cloud.work.entity.ProjectInfo;
import com.cloud.work.repository.ProjectInfoRepository;
import com.cloud.work.service.ProjectInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class ProjectInfoServiceImpl implements ProjectInfoService {

    private final ProjectInfoRepository projectInfoRepository;

    @Override
    public List<ProjectInfo> findAll() {
        return projectInfoRepository.findAll();
    }

    @Override
    public ProjectInfo create(ProjectInfo projectInfo) {
        return projectInfoRepository.save(projectInfo);
    }

    @Override
    public boolean existsByName(String name) {
        return projectInfoRepository.existsByName(name);
    }

    @Override
    public void updateStatusById(Long id, String status) {
        projectInfoRepository.updateStatusById(id,status);
    }

    @Override
    public ProjectInfo update(ProjectInfo projectInfo) {
        return projectInfoRepository.save(projectInfo);
    }

    @Override
    public ProjectInfo getById(Long projectId) {
        return projectInfoRepository.findById(projectId).get();
    }
}
