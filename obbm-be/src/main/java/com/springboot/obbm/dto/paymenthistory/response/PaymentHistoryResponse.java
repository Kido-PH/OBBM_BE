package com.springboot.obbm.dto.paymenthistory.response;

import com.springboot.obbm.dto.contract.response.ContractResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentHistoryResponse {
    Integer id;
    String paymentMethod;
    Long orderCode;
    Long amountPaid;
    ContractResponse contract;
    LocalDateTime createdAt;
}
