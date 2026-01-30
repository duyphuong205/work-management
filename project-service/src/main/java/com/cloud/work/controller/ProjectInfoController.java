package com.cloud.work.controller;

import com.cloud.work.dto.request.CreateProjectRequest;
import com.cloud.work.dto.request.UpdateProjectRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.facade.ProjectInfoFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectInfoController {

    private final ProjectInfoFacade projectInfoFacade;

    @PostMapping("/create")
    public ResponseEntity<?> doCreate(@Valid @RequestBody CreateProjectRequest createProjectRequest) {
        AppResponse appResponse = projectInfoFacade.createProject(createProjectRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> doDelete(@PathVariable Long id) {
        AppResponse appResponse = projectInfoFacade.deleteProject(id);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> doEdit(@PathVariable Long id) {
        AppResponse appResponse = projectInfoFacade.getProjectInfoById(id);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> doUpdate(@Valid @RequestBody UpdateProjectRequest updateProjectRequest) {
        AppResponse appResponse = projectInfoFacade.updateProject(updateProjectRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
}