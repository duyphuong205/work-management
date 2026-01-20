package com.cloud.work.dto.response;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.utils.MessageUtils;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppResponse implements Serializable {
    String code;
    String message;
    Object data;

    public static AppResponse success() {
        return AppResponse.builder()
                .code(AppConstants.RES_SUCCESS_CODE)
                .message(MessageUtils.getMessage(MessageConstants.MSG_SUCCESS))
                .build();
    }

    public static AppResponse success(Object data) {
        return AppResponse.builder()
                .code(AppConstants.RES_SUCCESS_CODE)
                .message(MessageUtils.getMessage(MessageConstants.MSG_SUCCESS))
                .data(data)
                .build();
    }

    public static AppResponse success(String messageKey, Object data) {
        return AppResponse.builder()
                .code(AppConstants.RES_SUCCESS_CODE)
                .message(MessageUtils.getMessage(messageKey))
                .data(data)
                .build();
    }

    public static AppResponse success(String code, String messageKey, Object data) {
        return AppResponse.builder()
                .code(code)
                .message(MessageUtils.getMessage(messageKey))
                .data(data)
                .build();
    }
}
