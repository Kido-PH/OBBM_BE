package com.springboot.obbm.dto.menu.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventForMenuResponse {
    Integer eventId;
    String name;
    Double totalcost;
    String description;
}