package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.event.request.EventRequest;
import com.springboot.obbm.dto.event.response.EventResponse;
import com.springboot.obbm.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEvent(EventRequest request);
    EventResponse toEventResponse(Event Event);
    void updateEvent(@MappingTarget Event Event, EventRequest request);
}
