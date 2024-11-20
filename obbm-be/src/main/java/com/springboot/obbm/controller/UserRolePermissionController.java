package com.springboot.obbm.controller;

import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.dto.userolepermission.request.UserRolePermissionRequest;
import com.springboot.obbm.dto.userolepermission.response.UserRolePermissionResponse;
import com.springboot.obbm.service.UserRolePermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-role-permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRolePermissionController {
    UserRolePermissionService userRolePermissionService;

    @PostMapping
    ApiResponse<UserRolePermissionResponse> saveUserRolePermissions(@RequestBody UserRolePermissionRequest request){
        return ApiResponse.<UserRolePermissionResponse>builder()
                .result(userRolePermissionService.saveUserRolePermissions(request))
                .build();
    }
}
