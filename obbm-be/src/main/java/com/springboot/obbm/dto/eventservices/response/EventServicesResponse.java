package com.springboot.obbm.dto.eventservices.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventServicesResponse {
    Integer eventserviceId;
    Integer quantity;
    Double cost;
    EventForEventServicesResponse events;
    ServiceForEventServicesResponse services;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}