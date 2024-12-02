package com.springboot.obbm.util;

import org.springframework.web.bind.annotation.InitBinder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CurrentDateVietnam {
    public static LocalDateTime getCurrentDateTime() {

        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime nowInVietnam = ZonedDateTime.now(vietnamZoneId).plusHours(7);
        return nowInVietnam.toLocalDateTime();
    }

    @InitBinder
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            System.out.println("Chuỗi đang phân tích cú pháp: " + dateTimeStr); // Kiểm tra chuỗi đầu vào
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi phân tích cú pháp ngày giờ: " + e.getMessage());
            throw e; // Ném ngoại lệ để xử lý ở nơi khác
        }
    }

}
