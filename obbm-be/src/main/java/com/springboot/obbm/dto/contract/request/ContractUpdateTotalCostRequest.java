package com.springboot.obbm.dto.contract.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractUpdateTotalCostRequest {
    double totalcost;
}