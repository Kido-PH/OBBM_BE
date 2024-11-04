package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.service.request.ServiceRequest;
import com.springboot.obbm.dto.service.response.ServiceResponse;
import com.springboot.obbm.models.Service;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    Service toService(ServiceRequest request);
    ServiceResponse toServiceResponse(Service service);
    void updateService(@MappingTarget Service service, ServiceRequest request);
}
