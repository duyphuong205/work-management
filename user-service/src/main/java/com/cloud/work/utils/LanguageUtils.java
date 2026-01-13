package com.cloud.work.utils;

import com.cloud.work.constants.AppConstants;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LanguageUtils {

    public static String getCurrentLanguage() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return AppConstants.LANGUAGE_VN;
        }
        String language = attributes.getRequest().getHeader("language");
        return StringUtils.hasText(language) ? AppConstants.LANGUAGE_VN : language;
    }
}
