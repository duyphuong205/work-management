package com.cloud.work.controller;

import com.cloud.work.dto.request.CreateProjectRequest;
import com.cloud.work.dto.response.AppResponse;
import com.cloud.work.facade.ProjectInfoFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectInfoController {
    private final ProjectInfoFacade projectInfoFacade;
    @PostMapping("/createProject")
    public ResponseEntity<?> createNewProject (@RequestBody CreateProjectRequest createProjectRequest){
        AppResponse appResponse = projectInfoFacade.createNewProject(createProjectRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
    }
    @GetMapping("/deleteProject/{id}")
    public ResponseEntity<?> deleteBoard (@PathVariable Long id  ){
        AppResponse appResponse = projectInfoFacade.deleteProject(id);
    return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
    @PostMapping("/updateNameProject/{id}")
    public ResponseEntity<?> updateNameBoard (@PathVariable Long id, @RequestBody CreateProjectRequest createProjectRequest){
        AppResponse appResponse = projectInfoFacade.updateNameBoard(id, createProjectRequest);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
}
