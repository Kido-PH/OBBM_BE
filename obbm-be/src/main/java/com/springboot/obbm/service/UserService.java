package com.springboot.obbm.service;

import com.springboot.obbm.constant.PredefinedRole;
import com.springboot.obbm.dto.request.UserCreationRequest;
import com.springboot.obbm.dto.request.UserUpdateRequest;
import com.springboot.obbm.dto.user.request.PasswordCreateRequest;
import com.springboot.obbm.dto.user.response.UserResponse;
import com.springboot.obbm.model.Role;
import com.springboot.obbm.model.User;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.UserMapper;
import com.springboot.obbm.respository.RoleRepository;
import com.springboot.obbm.respository.UserRepository;
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

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void createPassword(PasswordCreateRequest request){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));

        if(StringUtils.hasText(user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_EXISTED);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public UserResponse updateUser(String userID, UserUpdateRequest request) {
        User user = userRepository.findById(userID).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        userMapper.upadteUser(user, request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userID) {
        userRepository.deleteById(userID);
    }

    public UserResponse getMyInfo() {

        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));

        var userResponse = userMapper.toUserResponse(user);
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));

        return userResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('APPROVE_POST')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

//    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng")));
    }
}
