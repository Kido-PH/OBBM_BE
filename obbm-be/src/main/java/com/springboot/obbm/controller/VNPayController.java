package com.springboot.obbm.controller;

import com.springboot.obbm.dto.payment.request.CreatePaymentLinkRequest;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.dto.vnpay.PaymentDTO;
import com.springboot.obbm.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class VNPayController {
    private final VNPayService vnPayService;

    @PostMapping("/vn-pay")
    public ApiResponse<PaymentDTO.VNPayResponse> createPayment(HttpServletRequest httpRequest,
                                                               @RequestBody CreatePaymentLinkRequest request) {
        return ApiResponse.<PaymentDTO.VNPayResponse>builder()
                .code(1000)
                .message("Success")
                .result(vnPayService.createVnPayPayment(httpRequest, request))
                .build();
    }

    @GetMapping("/vn-pay-callback")
    public RedirectView payCallbackHandler(HttpServletRequest request) {
        try {
            vnPayService.handlePaymentCallback(request);
            String clientRedirectUrl = String.format(
                    "http://localhost:3000/payment/success?status=%s", request.getParameter("vnp_TransactionStatus")
            );
            return new RedirectView(clientRedirectUrl);
        } catch (Exception e) {
            String clientRedirectUrl = String.format(
                    "http://localhost:3000/payment/cancel?status=%s", request.getParameter("vnp_TransactionStatus")
            );
            e.printStackTrace();
            return new RedirectView(clientRedirectUrl);
        }
    }
}
