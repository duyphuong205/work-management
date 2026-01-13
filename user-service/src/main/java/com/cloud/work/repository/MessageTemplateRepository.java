package com.cloud.work.repository;

import com.cloud.work.entity.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
    MessageTemplate findByTypeAndLanguage(String type, String language);
}
