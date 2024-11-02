package com.springboot.obbm.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
    ErrorCode errorCode;

    Object[] params;

    public AppException(ErrorCode errorCode, Object... params) {
        super();
        this.errorCode = errorCode;
        this.params = params;
    }

    @Override
    public String getMessage() {
        return errorCode.formatMessage(params);
    }
}
