package com.springboot.obbm.dto.contract.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationForContractResponse {
    int locationId;
    String name;
    String type;
    String address;
    int capacity;
    int table;
    double cost;
    String description;
}