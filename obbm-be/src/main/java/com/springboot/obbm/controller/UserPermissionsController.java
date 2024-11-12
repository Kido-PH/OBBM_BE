package com.springboot.obbm.controller;

import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.dto.ugp.response.UserPermissionResponse;
import com.springboot.obbm.service.UserGroupPermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserPermissionsController {

    UserGroupPermissionService userGroupPermissionService;

    @GetMapping("/group/permissions")
    ApiResponse<UserPermissionResponse> getUserPermissions(){
        return ApiResponse.<UserPermissionResponse>builder()
                .result(userGroupPermissionService.finUserPermissions())
                .build();
    }
}
