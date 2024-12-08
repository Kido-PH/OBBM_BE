package com.springboot.obbm.dto.contract.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractResponse {
    int contractId;
    String name;
    String type;
    int guest;
    int table;
    double totalcost;
    String status;
    String paymentstatus;
    Double prepay;
    LocalDateTime organizdate;
    String description;
    String custname;
    String custphone;
    String custmail;
    UserForContractResponse users;
    LocationForContractResponse locations;
    EventForContractResponse events;
    MenuForContractResponse menus;
    List<StockRequestForContractResponse> listStockrequests;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}