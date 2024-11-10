package com.springboot.obbm.dto.event.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.contract.response.EventForContractResponse;
import com.springboot.obbm.dto.contract.response.LocationForContractResponse;
import com.springboot.obbm.dto.contract.response.MenuForContractResponse;
import com.springboot.obbm.dto.contract.response.UserForContractResponse;
import com.springboot.obbm.dto.stockrequest.response.StockrequestResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractForEventResponse {
    int contractId;
    String name;
    String type;
    int guest;
    int table;
    double totalcost;
    String status;
    String paymentstatus;
    LocalDateTime organizdate;
    String description;
    String custname;
    String custphone;
    String custmail;
}