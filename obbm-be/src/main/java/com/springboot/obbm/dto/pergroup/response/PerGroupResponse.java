package com.springboot.obbm.dto.pergroup.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.response.PermissionResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerGroupResponse {
    String name;
    String description;
    List<PermissionResponse> listPermission;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}