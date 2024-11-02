package com.springboot.obbm.dto.menu.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuCreateRequest {
    String name;
    Double totalcost;
    String description;
    String userId;
    Integer eventId;
}
