package com.springboot.obbm.dto.user.response;

import com.springboot.obbm.dto.response.RoleResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userId;
    String username;
    String fullname;
    LocalDate dob;
    Set<RoleResponse> roles;
}
