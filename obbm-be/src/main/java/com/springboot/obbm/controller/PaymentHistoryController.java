package com.springboot.obbm.controller;

import com.springboot.obbm.dto.paymenthistory.response.PaymentHistoryResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.PaymentHistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paymentHistory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentHistoryController {
    PaymentHistoryService paymentHistoryService;

    @GetMapping("/{contractId}")
    ApiResponse<List<PaymentHistoryResponse>> getAll(@PathVariable Integer contractId){
        return ApiResponse.<List<PaymentHistoryResponse>>builder()
                .result(paymentHistoryService.getAllPaymentHistories(contractId))
                .build();
    }

}
