package com.cloud.work.repository;

import com.cloud.work.entity.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Long> {
}
