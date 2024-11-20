package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.menu.response.MenuResponse;
import com.springboot.obbm.dto.request.PermissionRequest;
import com.springboot.obbm.dto.request.UserUpdateRequest;
import com.springboot.obbm.dto.response.PermissionResponse;
import com.springboot.obbm.dto.user.response.RoleForUserResponse;
import com.springboot.obbm.dto.user.response.UserResponse;
import com.springboot.obbm.dto.request.UserCreationRequest;
import com.springboot.obbm.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void upadteUser(@MappingTarget User user, UserUpdateRequest request);

    default UserResponse toUserResponseRole(User user, List<UserRolePermission> urpList) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .dob(user.getDob())
                .email(user.getEmail())
                .phone(user.getPhone())
                .image(user.getImage())
                .citizenIdentity(user.getCitizenIdentity())
                .noPassword(!StringUtils.hasText(user.getPassword()))
                .roles(mapRolesFromUrp(urpList)) // Chuyển đổi từ UserRolePermission
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private Set<RoleForUserResponse> mapRolesFromUrp(List<UserRolePermission> urpList) {
        // Nhóm theo Role
        Map<Role, List<UserRolePermission>> groupedByRole = urpList.stream()
                .collect(Collectors.groupingBy(UserRolePermission::getRoles));

        // Tạo danh sách RoleForUserResponse
        return groupedByRole.entrySet().stream()
                .map(entry -> {
                    Role role = entry.getKey();
                    List<UserRolePermission> urpsForRole = entry.getValue();

                    Set<PermissionResponse> permissions = urpsForRole.stream()
                            .map(urp -> PermissionResponse.builder()
                                    .name(urp.getPermissions().getName())
                                    .description(urp.getPermissions().getDescription())
                                    .build())
                            .collect(Collectors.toSet());

                    return RoleForUserResponse.builder()
                            .name(role.getName())
                            .description(role.getDescription())
                            .permissions(permissions)
                            .build();
                })
                .collect(Collectors.toSet());
    }
}
