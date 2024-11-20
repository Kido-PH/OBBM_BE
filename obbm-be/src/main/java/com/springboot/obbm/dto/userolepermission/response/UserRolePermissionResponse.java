package com.springboot.obbm.dto.userolepermission.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRolePermissionResponse {
    String userId;
    List<String> roles;
    List<String> permissions;
}
