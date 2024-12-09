package com.springboot.obbm.service;

import com.springboot.obbm.dto.paymenthistory.response.PaymentHistoryResponse;
import com.springboot.obbm.mapper.PaymentHistoryMapper;
import com.springboot.obbm.respository.PaymentHistoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentHistoryService {
    PaymentHistoryRepository paymentHistoryRepository;
    PaymentHistoryMapper paymentHistoryMapper;

    public List<PaymentHistoryResponse> getAllPaymentHistories(int contractId) {
        return paymentHistoryRepository.findAllByContract_ContractIdOrderByCreatedAtDesc(contractId)
                .stream().map(paymentHistoryMapper::toPaymentHistoryResponse)
                .toList();

    }
}
