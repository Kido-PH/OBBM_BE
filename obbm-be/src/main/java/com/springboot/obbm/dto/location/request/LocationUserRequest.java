package com.springboot.obbm.dto.location.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationUserRequest {
    String name;
    String type;
    String address;
    String userId;
}