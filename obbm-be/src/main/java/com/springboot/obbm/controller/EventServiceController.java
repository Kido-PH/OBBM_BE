package com.springboot.obbm.controller;

import com.springboot.obbm.dto.eventservice.request.EventServiceRequest;
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
    private EventServicesService EventServicesService;

    @GetMapping
    ApiResponse<PageImpl<EventServicesResponse>> getAllDishes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<EventServicesResponse>>builder()
                    .result(EventServicesService.getAllEventServices(adjustedPage, size))
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
                    .result(EventServicesService.getEventServiceByEventId(menuId, adjustedPage, size))
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
                    .result(EventServicesService.getEventServiceByServiceId(dishId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<EventServicesResponse> getDish(@PathVariable int id) {
        return ApiResponse.<EventServicesResponse>builder()
                .result(EventServicesService.getEventServiceById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<EventServicesResponse> createDish(@RequestBody EventServiceRequest request) {
        return ApiResponse.<EventServicesResponse>builder()
                .result(EventServicesService.createEventService(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<EventServicesResponse> updateDish(@PathVariable int id, @RequestBody EventServiceRequest request) {
        return ApiResponse.<EventServicesResponse>builder()
                .result(EventServicesService.updateEventService(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteDish(@PathVariable int id) {
        EventServicesService.deleteEventService(id);
        return ApiResponse.builder()
                .message("Dịch vụ sự kiện đã bị xóa.")
                .build();
    }

}
