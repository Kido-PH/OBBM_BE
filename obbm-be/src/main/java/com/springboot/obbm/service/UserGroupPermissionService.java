package com.springboot.obbm.service;

import com.springboot.obbm.dto.ugp.response.PerGroupResponse;
import com.springboot.obbm.dto.ugp.response.PermissionResponse;
import com.springboot.obbm.dto.ugp.response.UserPermissionResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.PermissionMapper;
import com.springboot.obbm.model.User;
import com.springboot.obbm.model.UserGroupPermission;
import com.springboot.obbm.respository.UGPRespository;
import com.springboot.obbm.respository.UserRespository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserGroupPermissionService {
    UGPRespository ugpRespository;
    UserRespository userRespository;

    public UserPermissionResponse finUserPermissions() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRespository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        List<UserGroupPermission> permissions = ugpRespository.findByUserId(user.getUserId());

        Map<String, PerGroupResponse> perGroupMap = new HashMap<>();
        for (UserGroupPermission ugp : permissions) {
            String groupName = ugp.getPergroups().getName();
            String groupDesc = ugp.getPergroups().getDescription();
            String permissionName  = ugp.getPermissions().getName();
            String permissionDesc = ugp.getPermissions().getDescription();

            perGroupMap.putIfAbsent(groupName, new PerGroupResponse(groupName,groupDesc, new ArrayList<>()));

            PermissionResponse permissionResponse = new PermissionResponse();
            permissionResponse.setName(permissionName);
            permissionResponse.setDescription(permissionDesc);

            perGroupMap.get(groupName).getListPermission().add(permissionResponse);
        }

        UserPermissionResponse response = new UserPermissionResponse();
        response.setUserId(user.getUserId());
        response.setListPergroup(new ArrayList<>(perGroupMap.values()));
        return response;
    }
}