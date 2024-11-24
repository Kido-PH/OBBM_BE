package com.springboot.obbm.dto.payment.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePaymentLinkRequest {
    Integer contractId;
    String productName;
    String description;
    Double prepay;
    String returnUrl;
    String cancelUrl;
}
