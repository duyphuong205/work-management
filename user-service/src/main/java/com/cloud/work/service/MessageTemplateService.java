package com.cloud.work.service;

import com.cloud.work.entity.MessageTemplate;

public interface MessageTemplateService {
    MessageTemplate getByTypeAndLanguage(String type, String language);
}
