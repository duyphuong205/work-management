package com.cloud.work.service.impl;

import com.cloud.work.service.ProjectMemberInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class ProjectMemberInfoServiceImpl implements ProjectMemberInfoService {
}
