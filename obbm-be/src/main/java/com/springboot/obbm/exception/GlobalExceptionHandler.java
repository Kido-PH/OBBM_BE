package com.springboot.obbm.exception;

import com.springboot.obbm.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("RuntimeException occurred: ", exception);  // Log thông tin lỗi
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> accessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        String formattedMessage = errorCode.getMessage();

        var bindingResult = exception.getBindingResult();

        // Kiểm tra lỗi của trường cụ thể (FieldError)
        var fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            String field = fieldError.getField();
            String enumKey = fieldError.getDefaultMessage(); // Sử dụng message cấu hình trong annotation

            try {
                errorCode = ErrorCode.valueOf(enumKey);
            } catch (IllegalArgumentException e) {
                log.warn("Error code not found for key: {}", enumKey);
            }

            formattedMessage = errorCode.getMessage().replace("{field}", field);

            // Kiểm tra các thuộc tính của annotation để tùy chỉnh thông báo
            ConstraintViolation<?> violation = fieldError.unwrap(ConstraintViolation.class);
            if (violation != null) {
                Map<String, Object> attributes = violation.getConstraintDescriptor().getAttributes();

                if (attributes.containsKey("value")) {
                    String value = attributes.get("value").toString();
                    if (enumKey.equals("FIELD_MIN")) {
                        formattedMessage = formattedMessage.replace("{min}", value);
                    } else if (enumKey.equals("FIELD_MAX")) {
                        formattedMessage = formattedMessage.replace("{max}", value);
                    }
                } else {
                    if (attributes.containsKey("min")) {
                        String minLength = attributes.get("min").toString();
                        formattedMessage = formattedMessage.replace("{min}", minLength);
                    }
                    if (attributes.containsKey("max")) {
                        String maxLength = attributes.get("max").toString();
                        formattedMessage = formattedMessage.replace("{max}", maxLength);
                    }
                }
            }
        } else {
            var globalError = bindingResult.getGlobalError();
            if (globalError != null) {
                String enumKey = globalError.getDefaultMessage();

                try {
                    errorCode = ErrorCode.valueOf(enumKey);
                    formattedMessage = errorCode.getMessage();
                } catch (IllegalArgumentException e) {
                    log.warn("Error code not found for key: {}", enumKey);
                    formattedMessage = enumKey;
                }
            }
        }

        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(formattedMessage)
                        .build());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_ENTRY;
        String errorMessage = "Đã phát hiện mục nhập trùng lặp: " + exception.getMostSpecificCause().getMessage();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorMessage)
                        .build());
    }
}
