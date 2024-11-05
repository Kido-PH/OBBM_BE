package com.springboot.obbm.dto.event.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.eventservice.response.ServiceForEventServicesResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventServicesForEventResponse {
    Integer eventserviceId;
    Integer quantity;
    Double cost;
    ServiceForEventServicesResponse services;
}