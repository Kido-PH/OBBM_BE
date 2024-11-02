package com.springboot.obbm.dto.contract.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuForContractResponse {
    int menuId;
    String name;
    String description;
}