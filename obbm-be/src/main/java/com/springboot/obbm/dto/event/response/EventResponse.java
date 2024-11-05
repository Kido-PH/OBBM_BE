package com.springboot.obbm.dto.event.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponse {
    Integer eventId;
    String name;
    Double totalcost;
    String description;
    List<EventServicesForEventResponse> listEventServices;
//    List<ContractForEventResponse> listContract;
    List<MenuForEventResponse> listMenu;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}