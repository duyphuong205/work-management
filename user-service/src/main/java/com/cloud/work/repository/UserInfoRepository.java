package com.cloud.work.repository;

import com.cloud.work.entity.UserInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByEmail(String email);
    boolean existsByEmail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE UserInfo u SET u.status = :status WHERE u.email = :email")
    int updateStatusByEmail(String email, String status);
}
