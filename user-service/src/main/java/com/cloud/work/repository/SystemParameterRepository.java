package com.cloud.work.repository;

import com.cloud.work.entity.SystemParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemParameterRepository extends JpaRepository<SystemParameter, Long> {
    SystemParameter findByTypeAndCode(String type, String code);
}
