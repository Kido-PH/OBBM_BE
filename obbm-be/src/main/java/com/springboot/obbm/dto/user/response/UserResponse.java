package com.springboot.obbm.dto.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.response.RoleResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    String email;
    String phone;
    String image;
    String citizenIdentity;
    Boolean noPassword = false;
    Set<RoleForUserResponse> roles;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
