package com.springboot.obbm.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa được phân loại", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Lỗi chưa được phân loại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Tên đăng nhập phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Mật khẩu phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Tuổi của bạn phải ít nhất {min}", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1009, "Danh mục không tồn tại", HttpStatus.NOT_FOUND),
    DISH_NOT_EXISTED(1010, "Món ăn không tồn tại", HttpStatus.NOT_FOUND),
    ;

    int code;
    String message;
    HttpStatus httpStatus;
}
