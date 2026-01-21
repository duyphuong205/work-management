package com.cloud.work.repository;

import com.cloud.work.entity.ProjectMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberInfoRepository extends JpaRepository<ProjectMemberInfo, Long> {
}
