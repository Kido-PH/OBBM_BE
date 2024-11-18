package com.springboot.obbm.controller;

import com.springboot.obbm.dto.event.request.EventRequest;
import com.springboot.obbm.dto.event.response.EventResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.EventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {
    private EventService eventService;

    @GetMapping
    ApiResponse<PageImpl<EventResponse>> getAllEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<EventResponse>>builder()
                    .result(eventService.getAllEvents(adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAlLEventUser")
    ApiResponse<PageImpl<EventResponse>> getAllUserEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<EventResponse>>builder()
                    .result(eventService.getAllAdminOrUserEvents(false, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAlLEventAdmin")
    ApiResponse<PageImpl<EventResponse>> getAllAdminOrUserEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<EventResponse>>builder()
                    .result(eventService.getAllAdminOrUserEvents(true, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllEvent/{userId}")
    ApiResponse<PageImpl<EventResponse>> getAllEventsByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<EventResponse>>builder()
                    .result(eventService.getAllEventsByUserId(userId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/latestEvent/{userId}")
    ApiResponse<EventResponse> getLatestMenuByUserId(@PathVariable String userId) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.getLatestEventByUserId(userId))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<EventResponse> getEvent(@PathVariable int id) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.getEventById(id))
                .build();
    }

    @PostMapping("/admin")
    ApiResponse<EventResponse> createAdminEvent(@RequestBody EventRequest request) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.createAdminEvent(request))
                .build();
    }

    @PostMapping("/user")
    ApiResponse<EventResponse> createUserEvent(@RequestBody EventRequest request) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.createUserEvent(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<EventResponse> updateEvent(@PathVariable int id, @RequestBody EventRequest request) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.updateEvent(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
        return ApiResponse.builder()
                .message("Sự kiện đã bị xóa.")
                .build();
    }

}
