package com.cloud.work.repository;

import com.cloud.work.entity.TokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenHistoryRepository extends JpaRepository<TokenHistory, Long> {
}
