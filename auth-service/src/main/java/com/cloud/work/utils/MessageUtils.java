package com.cloud.work.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Log4j2
@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    public static String getMessage(String key) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(key, null, locale);
        } catch (Exception e) {
            log.error(">>>Cannot get message {}", e.getMessage(), e);
            return key;
        }
    }

    public static String getMessage(String key, String language) {
        try {
            Locale locale = Locale.forLanguageTag(language.replace("_", "-"));
            return messageSource.getMessage(key, null, locale);
        } catch (Exception e) {
            log.error(">>>Cannot get message {}", e.getMessage(), e);
            return key;
        }
    }
}
