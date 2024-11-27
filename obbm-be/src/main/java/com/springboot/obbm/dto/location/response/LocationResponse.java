package com.springboot.obbm.dto.location.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.contract.response.UserForContractResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationResponse {
    int locationId;
    String name;
    String type;
    String address;
    Integer capacity;
    Integer table;
    Double cost;
    String image;
    String description;
    Boolean isCustom;
    String status;
    UserForContractResponse users;
    List<ContractForLocationResponse> listContract;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}