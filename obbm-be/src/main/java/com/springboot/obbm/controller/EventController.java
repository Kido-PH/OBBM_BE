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
        try{
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<EventResponse>>builder()
                    .result(eventService.getAllEvents(adjustedPage,size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<EventResponse> getCategory(@PathVariable int id) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.getEventById(id))
                .build();
    }

    @PostMapping
    ApiResponse<EventResponse> createCategory(@RequestBody EventRequest request){
        return ApiResponse.<EventResponse>builder()
                .result(eventService.createEvent(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<EventResponse> updateCategory(@PathVariable int id, @RequestBody EventRequest request){
        return ApiResponse.<EventResponse>builder()
                .result(eventService.updateEvent(id,request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteCategory(@PathVariable int id) {
        eventService.deleteEvent(id);
        return ApiResponse.builder()
                .message("Sự kiện đã bị xóa.")
                .build();
    }

}
