package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.userolepermission.response.UserRolePermissionResponse;
import com.springboot.obbm.model.User;
import com.springboot.obbm.model.UserRolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserRolePermissionMapper {

    @Mapping(source = "user.userId", target = "userId") // Lấy userId từ User
    @Mapping(source = "userRolePermissions", target = "roles", qualifiedByName = "mapRoles")
    @Mapping(source = "userRolePermissions", target = "permissions", qualifiedByName = "mapPermissions")
    UserRolePermissionResponse toResponse(User user, List<UserRolePermission> userRolePermissions);

    @Named("mapRoles")
    default List<String> mapRoles(List<UserRolePermission> userRolePermissions) {
        return userRolePermissions.stream()
                .map(urp -> urp.getRoles().getName()) // Đảm bảo roles và getName tồn tại
                .distinct()
                .collect(Collectors.toList());
    }

    @Named("mapPermissions")
    default List<String> mapPermissions(List<UserRolePermission> userRolePermissions) {
        return userRolePermissions.stream()
                .map(urp -> urp.getPermissions().getName()) // Đảm bảo permissions và getName tồn tại
                .distinct()
                .collect(Collectors.toList());
    }
}

