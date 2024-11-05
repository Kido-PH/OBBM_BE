package com.springboot.obbm.service;

import com.springboot.obbm.dto.event.request.EventRequest;
import com.springboot.obbm.dto.event.response.EventResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.EventMapper;
import com.springboot.obbm.model.Event;
import com.springboot.obbm.respository.EventRespository;
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
    EventRespository eventRespository;
    EventMapper eventMapper;

    public PageImpl<EventResponse> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRespository.findAllByDeletedAtIsNull(pageable);

        var responseList = eventPage.getContent().stream()
                .distinct()
                .map(event -> {
                    event.setListEventServices(
                            event.getListEventServices().stream()
                                    .filter(eventService -> eventService.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );

/*//                    event.setListContract(
//                            event.getListContract().stream()
//                                    .filter(contract -> contract.getDeletedAt() == null)
//                                    .collect(Collectors.toList())
//                    );*/

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

    public EventResponse getEventById(int id) {
        Event event = eventRespository.findByEventIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));

/*//        event.setListContract(
//                event.getListContract().stream()
//                        .filter(contract -> contract.getDeletedAt() == null)
//                        .collect(Collectors.toList())
//        );*/

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

    public EventResponse createEvent(EventRequest request) {
        Event event = eventMapper.toEvent(request);
        event.setCreatedAt(LocalDateTime.now());
        return eventMapper.toEventResponse(eventRespository.save(event));
    }

    public EventResponse updateEvent(int id, EventRequest request) {
        Event event = eventRespository.findByEventIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        event.setUpdatedAt(LocalDateTime.now());
        eventMapper.updateEvent(event, request);
        return eventMapper.toEventResponse(eventRespository.save(event));
    }

    public void deleteEvent(int id) {
        Event event = eventRespository.findByEventIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));

        event.setDeletedAt(LocalDateTime.now());
        eventRespository.save(event);
    }
}
