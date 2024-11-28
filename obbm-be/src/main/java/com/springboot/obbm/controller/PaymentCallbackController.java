package com.springboot.obbm.controller;

import com.springboot.obbm.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentCallbackController {
    PaymentService paymentService;

    @GetMapping("/success")
    public RedirectView paymentSuccess(@RequestParam Integer contractId,
                                       @RequestParam Integer amountPaid,
                                       @RequestParam String status,
                                       @RequestParam Long orderCode) {
        paymentService.updateContractStatus(contractId, amountPaid, orderCode);

        String clientRedirectUrl = String.format(
                "http://localhost:3000/payment/success?status=%s", status
        );

        return new RedirectView(clientRedirectUrl);
    }

    @GetMapping("/cancel")
    public RedirectView paymentCancel(@RequestParam String status) {
        String clientRedirectUrl = String.format(
                "http://localhost:3000/payment/cancle?status=%s", status
        );

        return new RedirectView(clientRedirectUrl);
    }
}