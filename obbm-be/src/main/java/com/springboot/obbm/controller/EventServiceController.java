package com.springboot.obbm.controller;

import com.springboot.obbm.dto.eventservices.request.EventServicesRequest;
import com.springboot.obbm.dto.eventservices.response.EventServicesResponse;
import com.springboot.obbm.dto.menudish.request.MenuDishRequest;
import com.springboot.obbm.dto.menudish.response.MenuDishResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.EventServicesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/saveAllMenuDish")
    public ApiResponse<List<EventServicesResponse>> saveAllEventServices(@RequestBody List<EventServicesRequest> requestList) {
        return ApiResponse.<List<EventServicesResponse>>builder()
                .result(eventServicesService.saveAllEventServices(requestList))
                .build();
    }

    @PostMapping()
    public ApiResponse<EventServicesResponse> createAdminEventService(@RequestBody EventServicesRequest request) {
        return ApiResponse.<EventServicesResponse>builder()
                .result(eventServicesService.createEventService(request))
                .build();
    }


    @PutMapping("/{id}")
    public ApiResponse<EventServicesResponse> updateEventService(@PathVariable int id, @RequestBody EventServicesRequest request) {
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
