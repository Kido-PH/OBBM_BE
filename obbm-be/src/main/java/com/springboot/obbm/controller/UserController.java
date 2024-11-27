package com.springboot.obbm.controller;

import com.springboot.obbm.dto.user.request.UserCreateUserRequest;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.dto.user.request.PasswordCreateRequest;
import com.springboot.obbm.dto.user.request.UserUpdateUserRequest;
import com.springboot.obbm.dto.user.response.UserResponse;
import com.springboot.obbm.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserService userService; // Khởi tạo userService bằng @Autowired

    @PostMapping("/create-password")
    ApiResponse<Void> createPassword(@RequestBody @Valid PasswordCreateRequest request) {
        userService.createPassword(request);
        return ApiResponse.<Void>builder()
                .message("Mật khẩu đã được tạo, bạn có thể sử dụng nó để đăng nhập")
                .build();
    }

    @PostMapping("/user")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateUserRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @PutMapping("/user/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateUserRequest request) {
        return userService.updateUser(userId, request);
    }

//    @PostMapping("/staff")
//    ApiResponse<UserResponse> createStaff(@RequestBody @Valid UserCreateUserRequest request) {
//        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(userService.createStaff(request));
//        return apiResponse;
//    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("GrantedAuthority : {}", grantedAuthority));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    UserResponse getUserById(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
