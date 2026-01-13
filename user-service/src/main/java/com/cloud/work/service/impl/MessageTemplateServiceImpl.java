package com.cloud.work.service.impl;

import com.cloud.work.entity.MessageTemplate;
import com.cloud.work.repository.MessageTemplateRepository;
import com.cloud.work.service.MessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private final MessageTemplateRepository messageTemplateRepository;

    @Override
    public MessageTemplate getByTypeAndLanguage(String type, String language) {
        return messageTemplateRepository.findByTypeAndLanguage(type, language);
    }
}
