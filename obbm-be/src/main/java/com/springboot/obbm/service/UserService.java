package com.springboot.obbm.service;

import com.springboot.obbm.constant.PredefinedRole;
import com.springboot.obbm.dto.dish.request.DishRequest;
import com.springboot.obbm.dto.dish.response.DishResponse;
import com.springboot.obbm.dto.user.request.UserCreateUserRequest;
import com.springboot.obbm.dto.user.request.PasswordCreateRequest;
import com.springboot.obbm.dto.user.request.UserUpdateUserRequest;
import com.springboot.obbm.dto.user.response.UserResponse;
import com.springboot.obbm.model.*;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.UserMapper;
import com.springboot.obbm.respository.RoleRepository;
import com.springboot.obbm.respository.UserRepository;
import com.springboot.obbm.respository.UserRolePermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserRolePermissionRepository userRolePermissionRepository;
    UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

//    public UserResponse createStaff(UserCreateUserRequest request) {
//        User user = userMapper.toUser(request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setCreatedAt(LocalDateTime.now());
//
//        Role staffRole = roleRepository.findById(PredefinedRole.STAFF_ROLE).orElseThrow(
//                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Quyền"));
//        user.setRoles(Set.of(staffRole));
//
//        userRepository.save(user);
//        List<UserRolePermission> urpList = createUserRolePermissions(user, staffRole);
//
//        userRolePermissionRepository.saveAll(urpList);
//        List<UserRolePermission> persistedUrpList = userRolePermissionRepository.findByUsers(user);
//
//        return userMapper.toUserResponseRole(user, persistedUrpList);
//    }

    public UserResponse createUser(UserCreateUserRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        Role userRole = roleRepository.findById(PredefinedRole.USER_ROLE).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Quyền"));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
        List<UserRolePermission> urpList = createUserRolePermissions(user, userRole);

        userRolePermissionRepository.saveAll(urpList);
        List<UserRolePermission> persistedUrpList = userRolePermissionRepository.findByUsers(user);

        return userMapper.toUserResponseRole(user, persistedUrpList);
    }

//    public UserResponse updateUser(String userID, UserUpdateUserRequest request) {
//        User user = userRepository.findById(userID).orElseThrow(
//                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
//        userMapper.upadteUser(user, request);
//
//        Role userRole = roleRepository.findById(PredefinedRole.USER_ROLE).orElseThrow(
//                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Quyền"));
//        user.setRoles(Set.of(userRole));
//
//        List<UserRolePermission> urpListRole = userRolePermissionRepository.findByUsers(user);
//        userRepository.save(user);
//        return userMapper.toUserResponseRole(user, urpListRole);
//    }

    public UserResponse updateUser(String userID, UserUpdateUserRequest request){
        User user = userRepository.findById(userID).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        userMapper.upadteUser(user, request);
        user.setCreatedAt(LocalDateTime.now());
        userMapper.upadteUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void createPassword(PasswordCreateRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));

        if (StringUtils.hasText(user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_EXISTED);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }


//    public UserResponse updateStaff(String userID, UserUpdateStaffRequest request) {
//        User user = userRepository.findById(userID).orElseThrow(
//                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
//        userMapper.upadteUser(user, request);
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        var roles = roleRepository.findAllById(request.getRoles());
//        user.setRoles(new HashSet<>(roles));
//
//        List<UserRolePermission> urpListRole = userRolePermissionRepository.findByUsers(user);
//
//        return userMapper.toUserResponseRole(user, urpListRole);
//    }



    public void deleteUser(String userID) {
        userRepository.deleteById(userID);
    }

    public UserResponse getMyInfo() {

        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));

        List<UserRolePermission> urpList = userRolePermissionRepository.findByUsers(user);

        return userMapper.toUserResponseRole(user, urpList);
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('APPROVE_POST')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            List<UserRolePermission> urpList = userRolePermissionRepository.findByUsers(user);
            return userMapper.toUserResponseRole(user, urpList);
        }).collect(Collectors.toList());
    }

//    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));

        List<UserRolePermission> urpList = userRolePermissionRepository.findByUsers(user);

        return userMapper.toUserResponseRole(user, urpList);
    }

    private List<UserRolePermission> createUserRolePermissions(User user, Role role) {
        return role.getPermissions().stream()
                .map(permission -> UserRolePermission.builder()
                        .users(user)
                        .roles(role)
                        .permissions(permission)
                        .createdAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
    }
}
