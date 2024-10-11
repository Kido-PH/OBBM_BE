package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.request.UserUpdateRequest;
import com.springboot.obbm.dto.response.UserResponse;
import com.springboot.obbm.dto.request.UserCreationRequest;
import com.springboot.obbm.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void upadteUser(@MappingTarget User user, UserUpdateRequest request);
}