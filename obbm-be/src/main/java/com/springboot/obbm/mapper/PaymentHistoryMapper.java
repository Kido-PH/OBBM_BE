package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.paymenthistory.response.PaymentHistoryResponse;
import com.springboot.obbm.model.PaymentHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentHistoryMapper {
    PaymentHistoryResponse toPaymentHistoryResponse(PaymentHistory paymentHistory);
}
