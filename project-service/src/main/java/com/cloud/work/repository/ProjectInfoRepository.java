package com.cloud.work.repository;

import com.cloud.work.entity.ProjectInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Long> {
    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectInfo u SET u.status = :status WHERE u.projectId = :id")
    void updateStatusById(Long id, String status);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectInfo u SET u.name = :status WHERE u.projectId = :id")
    void updateNameById(Long id, String name);
}
