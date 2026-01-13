package com.cloud.work.controller;

import com.cloud.work.service.TaskInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskInfoService taskInfoService;

    @GetMapping
    public ResponseEntity<?> doGetAll() {
        return new ResponseEntity<>(taskInfoService.findAll(), HttpStatus.OK);
    }
}
