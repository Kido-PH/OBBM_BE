package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.location.request.LocationAdminRequest;
import com.springboot.obbm.dto.location.request.LocationUserRequest;
import com.springboot.obbm.dto.location.response.LocationResponse;
import com.springboot.obbm.models.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    @Mapping(target = "users", ignore = true)
    Location toLocationUser(LocationUserRequest request);
    Location toLocationAdmin(LocationAdminRequest request);
    LocationResponse toLocationResponse(Location location);
    @Mapping(target = "users", ignore = true)
    void updateLocation(@MappingTarget Location category, LocationAdminRequest request);
}
