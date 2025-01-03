package com.springboot.obbm.dto.payment.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePaymentLinkRequest {
    Integer contractId;
    String productName;
    String description;
    Long prepay;
}
