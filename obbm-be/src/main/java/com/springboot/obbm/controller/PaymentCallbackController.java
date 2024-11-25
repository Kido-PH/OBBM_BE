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

//    @GetMapping("/success")
//    public ApiResponse<String> paymentSuccess(@RequestParam Integer contractId) {
//        return paymentService.getContractStatusResponse(contractId);
//    }

    @GetMapping("/success")
    public ApiResponse<String> paymentSuccess(@RequestParam Integer contractId, @RequestParam Integer amountPaid) {
        paymentService.updateContractStatus(contractId, amountPaid);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(200)
                .message("Thanh toán thành công!")
                .result("success")
                .build();
        return response;
    }


    @GetMapping("/cancel")
    public ApiResponse<String> paymentCancel() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(400)
                .message("Thanh toán đã bị hủy.")
                .result("cancel")
                .build();
        return response;
    }
}