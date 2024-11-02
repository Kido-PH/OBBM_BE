package com.springboot.obbm.dto.menu.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuUpdateRequest {
    String name;
    Double totalcost;
    String description;
    Integer eventId;
}
