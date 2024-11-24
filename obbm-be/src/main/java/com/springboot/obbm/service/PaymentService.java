package com.springboot.obbm.service;

import com.springboot.obbm.dto.payment.request.CreatePaymentLinkRequest;
import com.springboot.obbm.dto.payment.response.PaymentLinkResponse;
import com.springboot.obbm.model.Contract;
import com.springboot.obbm.respository.ContractRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    public PaymentLinkResponse createPaymentLink(CreatePaymentLinkRequest request) {
        try {
            // Sinh mã đơn hàng
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
                    .returnUrl(request.getReturnUrl() + "?contractId=" + request.getContractId() + "&amountPaid=" + pricePrePay)
                    .cancelUrl(request.getCancelUrl())
                    .item(item)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            return new PaymentLinkResponse(data.getCheckoutUrl(), 0, "success");
        } catch (Exception e) {
            return new PaymentLinkResponse(null, -1, e.getMessage());
        }
    }

    public void handlePaymentWebhook(Map<String, Object> payload) {
        String status = (String) payload.get("status");
        String orderCode = (String) payload.get("orderCode");
        int amount = (int) payload.get("amount");
        Integer contractId = (Integer) payload.get("contractId");

        try {
            if ("success".equalsIgnoreCase(status)) {

                updateContractStatus(contractId, amount);
            } else if ("cancel".equalsIgnoreCase(status)) {
                log.info("Payment cancelled for order: {}", orderCode);
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

            log.info("Updating contract {} with totalcost: {} and paymentstatus: {}",
                    contractId, amountPaid, contract.getPaymentstatus());

            contractRepository.save(contract);
            log.info("Contract {} updated with payment status {}", contractId, contract.getPaymentstatus());
        } else {
            log.error("Contract not found for ID {}", contractId);
        }
    }

}
