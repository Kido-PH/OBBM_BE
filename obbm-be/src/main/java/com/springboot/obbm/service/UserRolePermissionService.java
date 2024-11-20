package com.springboot.obbm.service;

import com.springboot.obbm.dto.userolepermission.request.UserRolePermissionRequest;
import com.springboot.obbm.dto.userolepermission.response.UserRolePermissionResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.UserRolePermissionMapper;
import com.springboot.obbm.model.Permission;
import com.springboot.obbm.model.Role;
import com.springboot.obbm.model.User;
import com.springboot.obbm.model.UserRolePermission;
import com.springboot.obbm.respository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserRolePermissionService {
     UserRolePermissionRepository userRolePermissionRepository;
     UserRepository userRepository;
     RoleRepository roleRepository;
     PermissionRepository permissionRepository;
     UserRolePermissionMapper userRolePermissionMapper;

    @Transactional
    public UserRolePermissionResponse saveUserRolePermissions(UserRolePermissionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, request.getUserId()));

        userRolePermissionRepository.deleteByUsers(user);

        List<UserRolePermission> userRolePermissions = new ArrayList<>();

        for (String roleName : request.getRoleNames()) {
            Role role = roleRepository.findById(roleName)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, roleName));

            for (String permissionName : request.getPermissionNames()) {
                Permission permission = permissionRepository.findById(permissionName)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, permissionName));

                UserRolePermission urp = UserRolePermission.builder()
                        .users(user)
                        .roles(role)
                        .permissions(permission)
                        .createdAt(LocalDateTime.now())
                        .build();

                userRolePermissions.add(urp);
            }
        }

        userRolePermissionRepository.saveAll(userRolePermissions);

        return userRolePermissionMapper.toResponse(user, userRolePermissions);
    }

}
