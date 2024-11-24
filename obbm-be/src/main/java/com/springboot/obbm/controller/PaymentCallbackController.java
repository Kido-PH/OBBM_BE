package com.springboot.obbm.controller;

import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentCallbackController {
    PaymentService paymentService;

    @GetMapping("/success")
    public ApiResponse<String> paymentSuccess(@RequestParam Integer contractId, @RequestParam Integer amountPaid) {
        System.out.println("Payment was successful! ContractId: " + contractId + ", Amount Paid: " + amountPaid);
        paymentService.updateContractStatus(contractId, amountPaid);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(200)
                .message("Payment was successful!")
                .result("success")
                .build();
        return response;
    }

    @GetMapping("/cancel")
    public ApiResponse<String> paymentCancel() {
        System.out.println("Payment was canceled.");

        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(400)
                .message("Payment was canceled.")
                .result("cancel")
                .build();
        return response;
    }
}