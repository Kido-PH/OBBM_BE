package com.springboot.obbm.dto.event.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserForEventResponse {
    String userId;
    String username;
    String fullname;
}