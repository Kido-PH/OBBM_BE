package com.springboot.obbm.dto.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.response.PermissionResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleForUserResponse {
    String name;
    String description;
    Set<PermissionResponse> permissions;
}
