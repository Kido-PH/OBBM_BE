package com.springboot.obbm.dto.contract.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventForContractResponse {
    int eventId;
    String name;
    double totalcost;
    String description;
}