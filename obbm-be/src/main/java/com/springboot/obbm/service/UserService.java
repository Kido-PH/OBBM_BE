package com.springboot.obbm.service;

import com.springboot.obbm.constant.PredefinedRole;
import com.springboot.obbm.dto.user.request.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
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

    public UserResponse updateUser(String userID, UserUpdateUserRequest request){
        User user = userRepository.findById(userID).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        userMapper.upadateUser(user, request);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.upadateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse createUserForAdmin(UserForAdminRequest request) {
        User user = userMapper.toUserForAdmin(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        Role userRole = roleRepository.findById(request.getRoles().get(0)).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Quyền"));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
        List<UserRolePermission> urpList = createUserRolePermissions(user, userRole);

        userRolePermissionRepository.saveAll(urpList);
        List<UserRolePermission> persistedUrpList = userRolePermissionRepository.findByUsers(user);

        return userMapper.toUserResponseRole(user, persistedUrpList);
    }

    @Transactional
    public UserResponse updateUserForAdmin(String userID, UserUpdaterForAdminRequest request) {
        User user = userRepository.findById(userID).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateForAdmin(user, request);

        userRolePermissionRepository.deleteByUsers(user);

        Role userRole = roleRepository.findById(request.getRoles().get(0)).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Quyền"));

        user.setRoles(new HashSet<>(Collections.singleton(userRole)));
        List<UserRolePermission> urpList = createUserRolePermissions(user, userRole);
        userRolePermissionRepository.saveAll(urpList);

        List<UserRolePermission> persistedUrpList = userRolePermissionRepository.findByUsers(user);
        return userMapper.toUserResponseRole(userRepository.save(user), persistedUrpList);
    }

    public void deleteUser(String userId) {
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
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
