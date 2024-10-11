package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.request.RoleRequest;
import com.springboot.obbm.dto.response.RoleResponse;
import com.springboot.obbm.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
