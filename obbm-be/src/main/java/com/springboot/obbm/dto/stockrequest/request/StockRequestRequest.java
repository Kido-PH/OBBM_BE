package com.springboot.obbm.dto.stockrequest.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockRequestRequest {
    Integer quantity;
    String approval;
    LocalDateTime receiveddate;
    String status;
}