package com.springboot.obbm.service;

import com.springboot.obbm.dto.eventservice.request.EventServiceAdminRequest;
import com.springboot.obbm.dto.eventservice.request.EventServiceUserRequest;
import com.springboot.obbm.dto.eventservice.response.EventServicesResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.EventServiceMapper;
import com.springboot.obbm.model.*;
import com.springboot.obbm.model.EventServices;
import com.springboot.obbm.respository.EventRepository;
import com.springboot.obbm.respository.EventServiceRepository;
import com.springboot.obbm.respository.ServicesRepository;
import com.springboot.obbm.respository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EventServicesService {
    EventServiceRepository eventServiceRepository;
    EventRepository eventRepository;
    ServicesRepository servicesRepository;
    UserRepository userRepository;
    EventServiceMapper eventServiceMapper;

    public PageImpl<EventServicesResponse> getAllEventServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventServices> eventServicePage = eventServiceRepository.findAllByDeletedAtIsNull(pageable);

        var responseList = eventServicePage.getContent().stream()
                .distinct().map(eventServiceMapper::toEventServiceResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, eventServicePage.getTotalElements());
    }

    public EventServicesResponse getEventServiceById(int id) {
        return eventServiceMapper.toEventServiceResponse(eventServiceRepository.findByEventserviceIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ sự kiện")));
    }

    public PageImpl<EventServicesResponse> getEventServiceByEventId(int menuId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventServices> EventServicePage = eventServiceRepository.findAllByEvents_EventIdAndDeletedAtIsNull(menuId, pageable);

        var responseList = EventServicePage.getContent().stream()
                .map(eventServiceMapper::toEventServiceResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, EventServicePage.getTotalElements());
    }

    public PageImpl<EventServicesResponse> getEventServiceByServiceId(int dishId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventServices> EventServicePage = eventServiceRepository.findAllByServices_ServiceIdAndDeletedAtIsNull(dishId, pageable);

        var responseList = EventServicePage.getContent().stream()
                .map(eventServiceMapper::toEventServiceResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, EventServicePage.getTotalElements());
    }

    public EventServicesResponse createUserEventService(EventServiceUserRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        Services services = servicesRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        EventServices eventServices = eventServiceMapper.toEventUserService(request);
        eventServices.setCreatedAt(LocalDateTime.now());
        eventServices.setEvents(event);
        eventServices.setServices(services);
        eventServices.setUsers(user);
        return eventServiceMapper.toEventServiceResponse(eventServiceRepository.save(eventServices));
    }

    public EventServicesResponse createAdminEventService(EventServiceAdminRequest request) {
        Services services = servicesRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ"));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        EventServices eventServices = eventServiceMapper.toEventAdminService(request);
        eventServices.setCreatedAt(LocalDateTime.now());
        eventServices.setEvents(event);
        eventServices.setServices(services);
        return eventServiceMapper.toEventServiceResponse(eventServiceRepository.save(eventServices));
    }

    public EventServicesResponse updateEventService(int id, EventServiceAdminRequest request) {
        EventServices EventServices = eventServiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn"));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        Services services = servicesRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Dịch vụ"));
        EventServices.setUpdatedAt(LocalDateTime.now());
        EventServices.setEvents(event);
        EventServices.setServices(services);
        eventServiceMapper.updateEventService(EventServices, request);
        return eventServiceMapper.toEventServiceResponse(eventServiceRepository.save(EventServices));
    }

    public void deleteEventService(int id) {
        EventServices EventServices = eventServiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn"));

        EventServices.setDeletedAt(LocalDateTime.now());
        eventServiceRepository.save(EventServices);
    }
}
