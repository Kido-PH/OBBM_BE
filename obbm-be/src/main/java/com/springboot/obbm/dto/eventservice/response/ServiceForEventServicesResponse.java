package com.springboot.obbm.dto.eventservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceForEventServicesResponse {
    Integer serviceId;
    String name;
    String type;
    Double price;
    String description;
    Boolean status;
}