package com.cloud.work.facade;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.request.CreateProjectRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.entity.ProjectInfo;
import com.cloud.work.entity.ProjectMemberInfo;
import com.cloud.work.enums.Role;
import com.cloud.work.enums.Status;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.service.ProjectInfoService;
import com.cloud.work.service.ProjectMemberInfoService;
import com.cloud.work.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectInfoFacade {
    private final ProjectInfoService projectInfoService;
    private final ProjectMemberInfoService projectMemberInfoService;

    public AppResponse createNewProject (CreateProjectRequest createProjectRequest){
        Timestamp createdTime = Timestamp.valueOf(
                LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        ProjectInfo projectInfo = new ProjectInfo();
        ProjectMemberInfo projectMemberInfo = new ProjectMemberInfo();
        String getName = createProjectRequest.getName();
        boolean checkName = projectInfoService.existsByName(getName);

        if(checkName){
            throw new BusinessException(AppConstants.RES_FAIL_CODE, MessageUtils.getMessage(MessageConstants.MSG_Create_Board_SUCCESS));
        }

        projectInfo.setName(createProjectRequest.getName());
        projectInfo.setStatus(Status.ACTV.name());
        projectInfo.setOwnerId(2L);
        projectInfo.setCreatedTime(createdTime);
        ProjectInfo save = projectInfoService.createNewProject(projectInfo);
        projectMemberInfo.setProjectId(save.getProjectId());
        projectMemberInfo.setRole(Role.OWNER);
        projectMemberInfo.setCreatedTime(createdTime);
        projectMemberInfoService.createProjectMember(projectMemberInfo);
        return AppResponse.success(MessageConstants.MSG_REGISTER_PENDING_ACTIVATION, projectInfo);
    }

    public AppResponse deleteProject(Long id) {
        projectInfoService.updateStatusById(id, Status.DLTD.name());
        return AppResponse.builder().code("200").message(MessageUtils.getMessage(MessageConstants.MSG_Delete_Board_SUCCESS)).build();
    }

    public AppResponse updateNameBoard(Long id, CreateProjectRequest createProjectRequest) {

        projectInfoService.updateNameBoard(id,createProjectRequest);
        return AppResponse.builder().code("200").message(MessageUtils.getMessage(MessageConstants.MSG_Delete_Board_SUCCESS)).build();

    }
}
