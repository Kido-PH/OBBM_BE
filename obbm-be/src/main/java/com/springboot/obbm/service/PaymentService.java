package com.springboot.obbm.service;

import com.springboot.obbm.dto.payment.request.CreatePaymentLinkRequest;
import com.springboot.obbm.dto.payment.response.PaymentLinkResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.model.Contract;
import com.springboot.obbm.respository.ContractRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.Date;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {
    PayOS payOS;
    ContractRepository contractRepository;

    @NonFinal
    @Value("${payos.success-url}")
    protected String successUrl;

    @NonFinal
    @Value("${payos.cancel-url}")
    protected String cancelUrl;

    public PaymentLinkResponse createPaymentLink(CreatePaymentLinkRequest request) {
        try {
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            int pricePrePay = (int) (request.getPrepay() * 1);

            ItemData item = ItemData.builder()
                    .name(request.getProductName())
                    .quantity(1)
                    .price(pricePrePay)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(pricePrePay)
                    .description(request.getDescription())
                    .returnUrl(successUrl + "?contractId=" + request.getContractId() + "&amountPaid=" + pricePrePay)
                    .cancelUrl(cancelUrl)
                    .item(item)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            return new PaymentLinkResponse(data.getCheckoutUrl(), 0, "success");
        } catch (Exception e) {
            return new PaymentLinkResponse(null, -1, e.getMessage());
        }
    }

    public void handlePaymentWebhook(Map<String, Object> payload) {
        try {
            String status = (String) payload.get("status");
            Integer contractId = (Integer) payload.get("contractId");
            int amountPaid = (int) payload.get("amount");

            if ("success".equalsIgnoreCase(status)) {
                updateContractStatus(contractId, amountPaid);
            } else if ("cancel".equalsIgnoreCase(status)) {
                log.info("Payment cancelled for contract: {}", contractId);
            } else {
                log.info("Unhandled payment status: {}", status);
            }
        } catch (Exception e) {
            log.error("Error handling payment webhook", e);
        }
    }

    @Transactional
    public void updateContractStatus(Integer contractId, Integer amountPaid) {
        Optional<Contract> contractOpt = contractRepository.findById(contractId);
        if (contractOpt.isPresent()) {
            Contract contract = contractOpt.get();

            double deposit = (amountPaid * 100) / contract.getTotalcost();
            String payStatus = "Unpaid";

            if(deposit < 50){
                payStatus = "Unpaid";
            } else if (deposit <100) {
                payStatus = "Prepay " + ((int) deposit) + "%";
            } else if (deposit >= 100){
                payStatus = "Paid";
            }

            contract.setPaymentstatus(payStatus);
            contract.setPrepay(amountPaid * 1.0);

            contractRepository.save(contract);
        } else {
            log.error("Contract not found for ID {}", contractId);
        }
    }

/*
//    @Transactional(readOnly = true)
//    public ApiResponse<String> getContractStatusResponse(Integer contractId) {
//        Optional<Contract> contractOpt = contractRepository.findById(contractId);
//        if (contractOpt.isPresent()) {
//            Contract contract = contractOpt.get();
//            return ApiResponse.<String>builder()
//                    .code(200)
//                    .message("Thanh toán thành công!")
//                    .result("Hợp đồng " + contractId + " có trạng thái: " + contract.getPaymentstatus())
//                    .build();
//        } else {
//            return ApiResponse.<String>builder()
//                    .code(404)
//                    .message("Hợp đồng không tồn tại.")
//                    .result("failed")
//                    .build();
//        }
//    }
*/

}
