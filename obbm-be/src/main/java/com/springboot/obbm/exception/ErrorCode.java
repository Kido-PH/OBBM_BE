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
    UNAUTHENTICATED(1006, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Tuổi của bạn phải ít nhất {min}", HttpStatus.BAD_REQUEST),
    OBJECT_NOT_EXISTED(1009, "%s không tồn tại", HttpStatus.NOT_FOUND),
    OBJECT_EXISTED(1010, "%s đã tồn tại", HttpStatus.NOT_FOUND),
    FIELD_NOT_BLANK(1011, "{field} không được bỏ trống!", HttpStatus.BAD_REQUEST),
    FIELD_FUTURE(1012, "{field} phải là thời gian trong tương lai!", HttpStatus.BAD_REQUEST),
    FIELD_MINVALID(1013, "{field} phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    FIELD_MAXVALID(1014, "{field} phải ít hơn {max} ký tự", HttpStatus.BAD_REQUEST),
    FIELD_MIN(1015, "{field} phải lớn hơn hoặc bằng {min}!", HttpStatus.BAD_REQUEST),
    FIELD_MAX(1016, "{field} phải bé hơn {max}!", HttpStatus.BAD_REQUEST),
    FIELD_ID_NOT_VALID(1017, "Id {field} chưa hợp lệ!", HttpStatus.BAD_REQUEST),
    TIME_RANGE_INVALID(1018, "Thời gian bắt đầu phải trước thời gian kết thúc", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_VALID(1019, "Email không hợp lệ!", HttpStatus.BAD_REQUEST),
    PRICE_NOT_VALID(1019, "{field} phải là số dương theo bội số của 1.000 đồng!", HttpStatus.BAD_REQUEST),
    DUPLICATE_ENTRY(1019, "Dữ liệu đã bị trùng lập!", HttpStatus.BAD_REQUEST),
    PASSWORD_EXISTED(1020, "Mật khẩu đã được tạo trước đó", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatus httpStatus;

    public String formatMessage(Object... params) {
        return String.format(message, params);
    }
}
