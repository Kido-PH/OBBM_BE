package com.springboot.obbm.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.obbm.service.PaymentService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentCallbackController {

    PaymentService paymentService;

/*//    @GetMapping("/success")
//    public ApiResponse<String> paymentSuccess(@RequestParam Integer contractId) {
//        return paymentService.getContractStatusResponse(contractId);
//    }*/

    @GetMapping("/success")
    public void paymentSuccess(@RequestParam Integer contractId, @RequestParam Integer amountPaid,
                               @RequestParam String code,
                               @RequestParam String id,
                               @RequestParam String cancel,
                               @RequestParam String status,
                               @RequestParam String orderCode,
                               HttpServletResponse response) throws IOException {
        paymentService.updateContractStatus(contractId, amountPaid);

        String redirectUrl = String.format("http://localhost:3000/obbm/payment/status?code=%s&id=%s&cancel=%s&status=%s&orderCode=%s",
                code, id, cancel, status, orderCode);

        // Redirect về FE
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/cancel")
    public void paymentCancel(
            @RequestParam String code,
            @RequestParam String id,
            @RequestParam String cancel,
            @RequestParam String status,
            @RequestParam String orderCode,
            HttpServletResponse response) throws IOException {

        // Xây dựng URL frontend với các tham số
        String redirectUrl = String.format(
                "http://localhost:3000/obbm/payment/status?code=%s&id=%s&cancel=%s&status=%s&orderCode=%s",
                code, id, cancel, status, orderCode
        );

        // Thực hiện redirect
        response.sendRedirect(redirectUrl);
    }
}
