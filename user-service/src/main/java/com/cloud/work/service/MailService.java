package com.cloud.work.service;

public interface MailService {
    void sendEmail(String to, String subject, String content);
}
