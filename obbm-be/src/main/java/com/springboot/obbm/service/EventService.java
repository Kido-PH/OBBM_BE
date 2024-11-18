package com.springboot.obbm.service;

import com.springboot.obbm.dto.event.request.EventRequest;
import com.springboot.obbm.dto.event.response.EventResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.EventMapper;
import com.springboot.obbm.model.Event;
import com.springboot.obbm.model.User;
import com.springboot.obbm.respository.EventRepository;
import com.springboot.obbm.respository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EventService {
    EventRepository eventRepository;
    UserRepository userRepository;
    EventMapper eventMapper;

    public PageImpl<EventResponse> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.findAllByDeletedAtIsNull(pageable);

        var responseList = eventPage.getContent().stream()
                .distinct()
                .map(event -> {
                    event.setListEventServices(
                            event.getListEventServices().stream()
                                    .filter(eventService -> eventService.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    event.setListMenu(
                            event.getListMenu().stream()
                                    .filter(menu -> menu.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    return eventMapper.toEventResponse(event);
                })
                .distinct()
                .toList();
        return new PageImpl<>(responseList, pageable, eventPage.getTotalElements());
    }

    public PageImpl<EventResponse> getAllAdminOrUserEvents(boolean isAdmin,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.findAllByIsmanagedAndDeletedAtIsNull(isAdmin, pageable);

        var responseList = eventPage.getContent().stream()
                .distinct()
                .map(event -> {
                    event.setListEventServices(
                            event.getListEventServices().stream()
                                    .filter(eventService -> eventService.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    event.setListMenu(
                            event.getListMenu().stream()
                                    .filter(menu -> menu.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    return eventMapper.toEventResponse(event);
                })
                .distinct()
                .toList();
        return new PageImpl<>(responseList, pageable, eventPage.getTotalElements());
    }

    public PageImpl<EventResponse> getAllEventsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.findAllByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId, pageable);

        var responseList = eventPage.getContent().stream()
                .distinct()
                .map(event -> {
                    event.setListEventServices(
                            event.getListEventServices().stream()
                                    .filter(eventService -> eventService.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    event.setListMenu(
                            event.getListMenu().stream()
                                    .filter(menu -> menu.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    return eventMapper.toEventResponse(event);
                })
                .distinct()
                .toList();
        return new PageImpl<>(responseList, pageable, eventPage.getTotalElements());
    }

    public EventResponse getLatestEventByUserId(String userId) {
        Event event = eventRepository.findTopByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));

        event.setListEventServices(
                event.getListEventServices().stream()
                        .filter(eventService -> eventService.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );
        event.setListMenu(
                event.getListMenu().stream()
                        .filter(menu -> menu.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );

        return eventMapper.toEventResponse(event);
    }

    public EventResponse getEventById(int id) {
        Event event = eventRepository.findByEventIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        event.setListMenu(
                event.getListMenu().stream()
                        .filter(menu -> menu.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );

        event.setListEventServices(
                event.getListEventServices().stream()
                        .filter(eventService -> eventService.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );

        return eventMapper.toEventResponse(event);
    }

    public EventResponse createAdminEvent(EventRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        Event event = eventMapper.toEvent(request);
        event.setUsers(user);
        event.setIsmanaged(true);
        event.setCreatedAt(LocalDateTime.now());
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    public EventResponse createUserEvent(EventRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        Event event = eventMapper.toEvent(request);
        event.setUsers(user);
        event.setIsmanaged(false);
        event.setCreatedAt(LocalDateTime.now());
        return eventMapper.toEventResponse(eventRepository.save(event));
    }


    public EventResponse updateEvent(int id, EventRequest request) {
        Event event = eventRepository.findByEventIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        event.setUpdatedAt(LocalDateTime.now());
        eventMapper.updateEvent(event, request);
        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    public void deleteEvent(int id) {
        Event event = eventRepository.findByEventIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));

        event.setDeletedAt(LocalDateTime.now());
        eventRepository.save(event);
    }
}
