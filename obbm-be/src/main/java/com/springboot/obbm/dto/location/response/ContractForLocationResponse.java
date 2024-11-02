package com.springboot.obbm.dto.location.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractForLocationResponse {
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