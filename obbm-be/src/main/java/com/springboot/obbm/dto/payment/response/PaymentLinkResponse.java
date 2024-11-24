package com.springboot.obbm.dto.payment.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentLinkResponse {
    private String checkoutUrl;
    private int error;
    private String message;
}
