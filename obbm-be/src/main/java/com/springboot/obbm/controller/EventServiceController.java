package com.springboot.obbm.controller;

import com.springboot.obbm.dto.eventservice.request.EventServiceAdminRequest;
import com.springboot.obbm.dto.eventservice.request.EventServiceUserRequest;
import com.springboot.obbm.dto.eventservice.response.EventServicesResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.EventServicesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventservice")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceController {
    private EventServicesService eventServicesService;

    @GetMapping
    ApiResponse<PageImpl<EventServicesResponse>> getAllDishes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<EventServicesResponse>>builder()
                    .result(eventServicesService.getAllEventServices(adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/byEvent")
    public ApiResponse<PageImpl<EventServicesResponse>> getEventServiceByEventId(
            @RequestParam int menuId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<EventServicesResponse>>builder()
                    .result(eventServicesService.getEventServiceByEventId(menuId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/byService")
    public ApiResponse<PageImpl<EventServicesResponse>> getEventServiceByServiceId(
            @RequestParam int dishId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<EventServicesResponse>>builder()
                    .result(eventServicesService.getEventServiceByServiceId(dishId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<EventServicesResponse> getEventServiceById(@PathVariable int id) {
        return ApiResponse.<EventServicesResponse>builder()
                .result(eventServicesService.getEventServiceById(id))
                .build();
    }

    @PostMapping("/admin")
    public ApiResponse<EventServicesResponse> createAdminEventService(@RequestBody EventServiceAdminRequest request) {
        return ApiResponse.<EventServicesResponse>builder()
                .result(eventServicesService.createAdminEventService(request))
                .build();
    }

    @PostMapping("/user")
    public ApiResponse<EventServicesResponse> createUserEventService(@RequestBody EventServiceUserRequest request) {
        return ApiResponse.<EventServicesResponse>builder()
                .result(eventServicesService.createUserEventService(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<EventServicesResponse> updateEventService(@PathVariable int id, @RequestBody EventServiceAdminRequest request) {
        return ApiResponse.<EventServicesResponse>builder()
                .result(eventServicesService.updateEventService(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteEventService(@PathVariable int id) {
        eventServicesService.deleteEventService(id);
        return ApiResponse.builder()
                .message("Dịch vụ sự kiện đã bị xóa.")
                .build();
    }

}
