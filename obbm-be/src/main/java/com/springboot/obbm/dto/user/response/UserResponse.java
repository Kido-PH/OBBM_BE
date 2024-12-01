package com.springboot.obbm.dto.user.response;

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
    Boolean gender;
    String residence;
    LocalDate dob;
    String email;
    String phone;
    String image;
    String citizenIdentity;
    Boolean isStatus = false;
    Boolean noPassword = false;
    Set<RoleForUserResponse> roles;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
