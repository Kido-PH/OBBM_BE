package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.request.PermissionRequest;
import com.springboot.obbm.dto.response.PermissionResponse;
import com.springboot.obbm.model.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
