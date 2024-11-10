package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.services.request.ServicesRequest;
import com.springboot.obbm.dto.services.response.ServicesResponse;
import com.springboot.obbm.model.Services;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServicesMapper {
    Services toService(ServicesRequest request);
    ServicesResponse toServiceResponse(Services services);
    void updateService(@MappingTarget Services services, ServicesRequest request);
}
