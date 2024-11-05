package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.eventservice.request.EventServiceRequest;
import com.springboot.obbm.dto.eventservice.response.EventServicesResponse;
import com.springboot.obbm.model.EventServices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventServiceMapper {
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "services", ignore = true)
    EventServices toEventService(EventServiceRequest request);

    EventServicesResponse toEventServiceResponse(EventServices EventServices);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "services", ignore = true)
    void updateEventService(@MappingTarget EventServices EventServices, EventServiceRequest request);
}
