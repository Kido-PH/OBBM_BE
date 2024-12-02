package com.springboot.obbm.controller;

import com.springboot.obbm.dto.contract.request.ContractRequest;
import com.springboot.obbm.dto.contract.request.ContractUpdateTotalCostRequest;
import com.springboot.obbm.dto.contract.response.ContractResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.ContractService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractController {
    private ContractService contractService;

    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getRevenueStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        LocalDateTime defaultStart = (startDate == null) ? LocalDateTime.now().withDayOfMonth(1) : startDate;
        LocalDateTime defaultEnd = (endDate == null) ? LocalDateTime.now() : endDate;

        Map<String, Object> statistics = contractService.getRevenueStatistics(defaultStart, defaultEnd);
        return ApiResponse.<Map<String, Object>>builder()
                .result(statistics)
                .build();
    }

    @GetMapping
    ApiResponse<PageImpl<ContractResponse>> getAllContracts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<ContractResponse>>builder()
                    .result(contractService.getAllContracts(adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("@securityUtil.isCurrentUser(#userId)")
    @GetMapping("/user/{userId}")
    ApiResponse<PageImpl<ContractResponse>> getAllContractsByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<ContractResponse>>builder()
                    .result(contractService.getAllContractsByUserId(userId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @PreAuthorize("@securityUtil.isCurrentUser(#userId)")
    @GetMapping("/latestContract/{id}")
    ApiResponse<ContractResponse> getLatestContract(@PathVariable String id) {
        return ApiResponse.<ContractResponse>builder()
                .result(contractService.getLatestContractByUserId(id))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ContractResponse> getContract(@PathVariable int id) {
        return ApiResponse.<ContractResponse>builder()
                .result(contractService.getContractById(id))
                .build();
    }

    @PostMapping
    ApiResponse<ContractResponse> createCategory(@RequestBody ContractRequest request) {
        return ApiResponse.<ContractResponse>builder()
                .result(contractService.createContract(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ContractResponse> updateContract(@PathVariable int id, @RequestBody ContractRequest request) {
        return ApiResponse.<ContractResponse>builder()
                .result(contractService.updateContract(id, request))
                .build();
    }


    @PutMapping("/totalCost/{id}")
    ApiResponse<ContractResponse> updateTotalCostContract(@PathVariable int id, @RequestBody ContractUpdateTotalCostRequest request) {
        return ApiResponse.<ContractResponse>builder()
                .result(contractService.updateTotalCostContract(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteCategory(@PathVariable int id) {
        contractService.deleteContract(id);
        return ApiResponse.builder()
                .message("Hợp đồng đã bị xóa.")
                .build();
    }

}
