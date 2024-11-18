package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.eventservices.request.EventServicesRequest;
import com.springboot.obbm.dto.eventservices.response.EventServicesResponse;
import com.springboot.obbm.model.EventServices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventServiceMapper {
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "services", ignore = true)
    EventServices toEventService(EventServicesRequest request);

    EventServicesResponse toEventServiceResponse(EventServices eventServices);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "services", ignore = true)
    void updateEventService(@MappingTarget EventServices EventServices, EventServicesRequest request);

    List<EventServicesResponse> toEventServicesResponseList(List<EventServices> eventServices);
}
