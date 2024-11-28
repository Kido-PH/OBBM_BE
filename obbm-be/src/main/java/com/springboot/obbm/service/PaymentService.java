package com.springboot.obbm.service;

import com.springboot.obbm.dto.payment.request.CreatePaymentLinkRequest;
import com.springboot.obbm.dto.payment.response.PaymentLinkResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.model.Contract;
import com.springboot.obbm.model.PaymentHistory;
import com.springboot.obbm.respository.ContractRepository;
import com.springboot.obbm.respository.PaymentHistoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.time.LocalDateTime;
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
    PaymentHistoryRepository paymentHistoryRepository;

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
            Long orderCode = Long.valueOf(payload.get("orderCode").toString());

            if ("success".equalsIgnoreCase(status)) {
                updateContractStatus(contractId, amountPaid, orderCode);
                log.info("Payment successful for contract ID {}", contractId);
            } else if ("cancel".equalsIgnoreCase(status)) {
                log.info("Payment cancelled for contract ID {}", contractId);
            } else {
                log.warn("Unhandled payment status: {}", status);
            }
        } catch (Exception e) {
            log.error("Error handling payment webhook", e);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateContractStatus(Integer contractId, Integer amountPaid, Long orderCode) {
        Optional<Contract> contractOpt = contractRepository.findById(contractId);
        if (contractOpt.isEmpty()) {
            log.error("Contract not found for ID {}", contractId);
            return;
        }
        Contract contract = contractOpt.get();

        if (paymentHistoryRepository.existsByOrderCode(orderCode)) {
            log.warn("Duplicate orderCode detected: {}", orderCode);
            return;
        }

        double currentTotalPaid = paymentHistoryRepository
                .findTotalPaidByContract_ContractId(contractId)
                .orElse(0.0);

        double newTotalPaid = currentTotalPaid + amountPaid;

        if (newTotalPaid > contract.getTotalcost()) {
            log.error("Payment exceeds total contract cost for contract ID {}", contractId);
            return;
        }

        double depositPercentage = (newTotalPaid * 100.0) / contract.getTotalcost();
        String payStatus;
        if (depositPercentage < 50) {
            payStatus = "Unpaid";
        } else if (depositPercentage < 100) {
            payStatus = "Prepay " + ((int) depositPercentage) + "%";
        } else {
            payStatus = "Paid";
        }

        contract.setPaymentstatus(payStatus);
        contract.setPrepay(newTotalPaid);
        contractRepository.save(contract);

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setContract(contract);
        paymentHistory.setAmountPaid(amountPaid);
        paymentHistory.setOrderCode(orderCode); // Lưu orderCode
        paymentHistory.setCreatedAt(LocalDateTime.now());
        paymentHistoryRepository.save(paymentHistory);

        log.info("Updated contract ID {}: Payment status {}, New Total Paid {}", contractId, payStatus, newTotalPaid);
    }
}
