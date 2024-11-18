package com.springboot.obbm.controller;

import com.springboot.obbm.dto.dishingredient.request.DishIngredientRequest;
import com.springboot.obbm.dto.dishingredient.response.DishIngredientResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.DishIngredientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishingredient")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DishIngredientController {
    private DishIngredientService dishIngredientService;

    @GetMapping
    ApiResponse<PageImpl<DishIngredientResponse>> getAllDishes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<DishIngredientResponse>>builder()
                    .result(dishIngredientService.getAllDishIngredients(adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @GetMapping("/byEvent")
//    public ApiResponse<PageImpl<DishIngredientResponse>> getEventServiceByEventId(
//            @RequestParam int menuId,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "5") int size) {
//
//        try {
//            int adjustedPage = (page > 0) ? page - 1 : 0;
//            return ApiResponse.<PageImpl<DishIngredientResponse>>builder()
//                    .result(eventServicesService.getEventServiceByEventId(menuId, adjustedPage, size))
//                    .build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @GetMapping("/byService")
//    public ApiResponse<PageImpl<DishIngredientResponse>> getEventServiceByServiceId(
//            @RequestParam int dishId,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "5") int size) {
//
//        try {
//            int adjustedPage = (page > 0) ? page - 1 : 0;
//            return ApiResponse.<PageImpl<DishIngredientResponse>>builder()
//                    .result(eventServicesService.getEventServiceByServiceId(dishId, adjustedPage, size))
//                    .build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @GetMapping("/{id}")
//    ApiResponse<DishIngredientResponse> getEventServiceById(@PathVariable int id) {
//        return ApiResponse.<DishIngredientResponse>builder()
//                .result(eventServicesService.getEventServiceById(id))
//                .build();
//    }

    @PostMapping("/saveAllDishIngredient")
    public ApiResponse<List<DishIngredientResponse>> saveAllEventServices(@RequestBody List<DishIngredientRequest> requestList) {
        return ApiResponse.<List<DishIngredientResponse>>builder()
                .result(dishIngredientService.saveAllDishIngredient(requestList))
                .build();
    }

//    @PostMapping()
//    public ApiResponse<DishIngredientResponse> createAdminEventService(@RequestBody EventServicesRequest request) {
//        return ApiResponse.<DishIngredientResponse>builder()
//                .result(eventServicesService.createEventService(request))
//                .build();
//    }
//
//
//    @PutMapping("/{id}")
//    public ApiResponse<DishIngredientResponse> updateEventService(@PathVariable int id, @RequestBody EventServicesRequest request) {
//        return ApiResponse.<DishIngredientResponse>builder()
//                .result(eventServicesService.updateEventService(id, request))
//                .build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<?> deleteEventService(@PathVariable int id) {
//        eventServicesService.deleteEventService(id);
//        return ApiResponse.builder()
//                .message("Dịch vụ sự kiện đã bị xóa.")
//                .build();
//    }

}
