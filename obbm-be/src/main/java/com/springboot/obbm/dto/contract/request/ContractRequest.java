package com.springboot.obbm.dto.contract.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractRequest {
    String name;
    String type;
    int guest;
    int table;
    double totalcost;
    String status;
    String paymentstatus;
    LocalDateTime organizdate;
    String custname;
    String custphone;
    String description;
    String userId;
    int locationId;
    int eventId;
    int menuId;
}