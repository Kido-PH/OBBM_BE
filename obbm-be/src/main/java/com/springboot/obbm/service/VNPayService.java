package com.springboot.obbm.service;

import com.springboot.obbm.configuration.VNPAYConfig;
import com.springboot.obbm.dto.payment.request.CreatePaymentLinkRequest;
import com.springboot.obbm.dto.vnpay.PaymentDTO;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.model.Contract;
import com.springboot.obbm.model.PaymentHistory;
import com.springboot.obbm.respository.ContractRepository;
import com.springboot.obbm.respository.PaymentHistoryRepository;
import com.springboot.obbm.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VNPayService {
    private final VNPAYConfig vnPayConfig;
    private final ContractRepository contractRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest httpRequest, CreatePaymentLinkRequest request) {
        try {
            Long amount = request.getPrepay() * 100L;
            String txnRef = request.getContractId() + "-" + System.currentTimeMillis();
            Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();

            vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
            vnpParamsMap.put("vnp_OrderInfo", "Payment for contract ID: " + request.getContractId());
            vnpParamsMap.put("vnp_TxnRef", txnRef);
            vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(httpRequest));

            String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
            String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
            String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
            queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
            String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

            return PaymentDTO.VNPayResponse.builder()
                    .code("ok")
                    .message("success")
                    .paymentUrl(paymentUrl).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create VNPay payment: " + e.getMessage());
        }
    }

    @Transactional
    public void handlePaymentCallback(HttpServletRequest request) {
        try {
            // Lấy các tham số từ callback của VNPay
            String responseCode = request.getParameter("vnp_ResponseCode");
            String txnRef = request.getParameter("vnp_TxnRef"); // Ví dụ: "5-1733067001515"
            Long amountPaid = (Long.parseLong(request.getParameter("vnp_Amount")) / 100);

            // Tách contractId từ txnRef
            String[] txnParts = txnRef.split("-");
            Integer contractId = Integer.parseInt(txnParts[0]); // Lấy phần đầu tiên là contractId
            Long orderCode = Long.parseLong(txnParts[1]); // Lấy phần thứ hai là mã giao dịch duy nhất

            // Kiểm tra kết quả thanh toán
            if ("00".equals(responseCode)) { // Mã "00" là thành công
                updateContractStatus(contractId, amountPaid, orderCode);
            } else {
                throw new RuntimeException("Payment failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error handling VNPay callback: " + e.getMessage());
        }
    }

    private void updateContractStatus(Integer contractId, Long amountPaid, Long orderCode) {
       Contract contract = contractRepository.findById(contractId).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Hợp đồng"));

        if (paymentHistoryRepository.existsByOrderCode(orderCode)) {
            throw new RuntimeException("Duplicate orderCode detected: " + orderCode);
        }

        double currentTotalPaid = paymentHistoryRepository
                .findTotalPaidByContract_ContractId(contractId)
                .orElse(0.0);

        double newTotalPaid = currentTotalPaid + amountPaid;

        String payStatus = getString(contractId, newTotalPaid, contract);

        contract.setPaymentstatus(payStatus);
        contract.setPrepay(newTotalPaid);
        contractRepository.save(contract);

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setContract(contract);
        paymentHistory.setAmountPaid(amountPaid);
        paymentHistory.setOrderCode(orderCode);
        paymentHistory.setCreatedAt(LocalDateTime.now());
        paymentHistoryRepository.save(paymentHistory);
    }

    private static @NotNull String getString(Integer contractId, double newTotalPaid, Contract contract) {
        if (newTotalPaid > contract.getTotalcost()) {
            throw new RuntimeException("Payment exceeds total contract cost for contract ID " + contractId);
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
        return payStatus;
    }
}
