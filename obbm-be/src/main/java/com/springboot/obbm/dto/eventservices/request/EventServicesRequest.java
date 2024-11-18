package com.springboot.obbm.dto.eventservices.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventServicesRequest {
    Integer quantity;
    Double cost;
    Integer eventId;
    Integer serviceId;
}