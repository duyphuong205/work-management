package com.cloud.work.service;

import jakarta.mail.MessagingException;

public interface MailService {
    void sendEmail(String to, String subject, String content) throws MessagingException;
}
