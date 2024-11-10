package com.springboot.obbm.dto.location.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationAdminRequest {
    String name;
    String type;
    String address;
    int capacity;
    int table;
    double cost;
    String image;
    String description;
    String status;
}