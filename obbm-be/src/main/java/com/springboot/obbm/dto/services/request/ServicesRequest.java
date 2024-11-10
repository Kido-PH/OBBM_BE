package com.springboot.obbm.dto.services.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicesRequest {
    String name;
    String type;
    Double price;
    String image;
    String description;
    Boolean status;
}