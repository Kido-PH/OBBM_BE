package com.springboot.obbm.dto.eventservice.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventServiceRequest {
    Integer quantity;
    Double cost;
    Integer eventId;
    Integer serviceId;
}