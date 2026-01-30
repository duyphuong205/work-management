package com.cloud.work.facade;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.CreateProjectRequest;
import com.cloud.work.dto.request.UpdateProjectRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.dto.response.CreateProjectResponse;
import com.cloud.work.dto.response.ProjectInfoResponse;
import com.cloud.work.dto.response.UpdateProjectResponse;
import com.cloud.work.entity.ProjectInfo;
import com.cloud.work.entity.ProjectMemberInfo;
import com.cloud.work.enums.Role;
import com.cloud.work.enums.Status;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.service.ProjectInfoService;
import com.cloud.work.service.ProjectMemberInfoService;
import com.cloud.work.utils.MessageUtils;
import com.cloud.work.utils.ObjectConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectInfoFacade {

    private final ProjectInfoService projectInfoService;
    private final ProjectMemberInfoService projectMemberInfoService;

    public AppResponse createProject(CreateProjectRequest createProjectRequest) {
        String name = createProjectRequest.getName().trim();
        boolean isExists = projectInfoService.existsByName(name);
        if (isExists) {
            throw new BusinessException(AppConstants.RES_DUPLICATE_CODE, MessageUtils.getMessage(MessageConstants.MSG_PROJECT_ALREADY_EXISTS));
        }

        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setName(name);
        projectInfo.setOwnerId(2L);
        projectInfo.setStatus(Status.ACTV.name());
        projectInfoService.create(projectInfo);

        ProjectMemberInfo projectMemberInfo = new ProjectMemberInfo();
        projectMemberInfo.setProjectId(projectInfo.getProjectId());
        projectMemberInfo.setRole(Role.OWNER);
        projectMemberInfoService.createProjectMember(projectMemberInfo);

        CreateProjectResponse createProjectResponse = ObjectConvertUtils.convertToDTO(projectInfo, CreateProjectResponse.class);
        return AppResponse.success(MessageConstants.MSG_CREATE_PROJECT_SUCCESS, createProjectResponse);
    }

    public AppResponse deleteProject(Long id) {
        projectInfoService.updateStatusById(id, Status.DLTD.name());
        return AppResponse.success(MessageConstants.MSG_DELETE_PROJECT_SUCCESS);
    }

    public AppResponse getProjectInfoById(Long id) {
        ProjectInfo projectInfo = projectInfoService.getById(id);
        ProjectInfoResponse projectInfoResponse = ObjectConvertUtils.convertToDTO(projectInfo, ProjectInfoResponse.class);
        return AppResponse.success(projectInfoResponse);
    }

    public AppResponse updateProject(UpdateProjectRequest updateProjectRequest) {
        ProjectInfo projectInfo = projectInfoService.getById(updateProjectRequest.getProjectId());
        projectInfo.setName(updateProjectRequest.getName());
        projectInfoService.update(projectInfo);

        UpdateProjectResponse updateProjectResponse = ObjectConvertUtils.convertToDTO(projectInfo, UpdateProjectResponse.class);
        return AppResponse.success(MessageConstants.MSG_UPDATE_PROJECT_SUCCESS, updateProjectResponse);
    }
}