package com.cloud.work.client.decoder;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.dto.response.ErrorResponse;
import com.cloud.work.exception.BusinessException;
import com.cloud.work.exception.NotFoundException;
import com.cloud.work.utils.MessageUtils;
import com.cloud.work.utils.ObjectConvertUtils;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = readBody(response);
        if (StringUtils.isEmpty(body)) {
            return defaultDecoder.decode(methodKey, response);
        }

        ErrorResponse err = parseError(body);
        if (Objects.isNull(err)) {
            return defaultDecoder.decode(methodKey, response);
        }

        String code = StringUtils.defaultIfEmpty(err.getCode(), AppConstants.RES_FAIL_CODE);
        String message = StringUtils.defaultIfEmpty(err.getMessage(), MessageUtils.getMessage(MessageConstants.MSG_SYSTEM_ERROR));

        if (response.status() == 404) {
            return new NotFoundException(code, message);
        }
        return new BusinessException(code, message);
    }

    private String readBody(Response response) {
        try {
            if (response.body() == null) {
                return null;
            }
            return Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error(">>>CustomErrorDecoder readBody() ERROR", e);
            return null;
        }
    }

    private ErrorResponse parseError(String body) {
        try {
            return ObjectConvertUtils.toObject(body, ErrorResponse.class);
        } catch (Exception e) {
            log.error(">>>CustomErrorDecoder parseError() ERROR", e);
            return null;
        }
    }
}