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
public class EventResponse {
    Integer eventId;
    String name;
    Double totalcost;
    String description;
    String image;
    Boolean ismanaged;
    UserForEventResponse users;
    List<EventServicesForEventResponse> listEventServices;
    List<MenuForEventResponse> listMenu;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}