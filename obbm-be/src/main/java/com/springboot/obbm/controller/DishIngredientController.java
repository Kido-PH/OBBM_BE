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
                    .result(dishIngredientService.getAllDishIngredient(adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/byDish")
    public ApiResponse<PageImpl<DishIngredientResponse>> getDishIngredientByDishId(
            @RequestParam int menuId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<DishIngredientResponse>>builder()
                    .result(dishIngredientService.getDishIngredientByDishId(menuId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/byIngredient")
    public ApiResponse<PageImpl<DishIngredientResponse>> getDishIngredientByIngredientId(
            @RequestParam int dishId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<DishIngredientResponse>>builder()
                    .result(dishIngredientService.getDishIngredientByIngredientId(dishId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<DishIngredientResponse> getDishIngredientById(@PathVariable int id) {
        return ApiResponse.<DishIngredientResponse>builder()
                .result(dishIngredientService.getDishIngredientById(id))
                .build();
    }

    @PostMapping("/saveAllDishIngredient")
    public ApiResponse<List<DishIngredientResponse>> saveAllDishIngredients(@RequestBody List<DishIngredientRequest> requestList) {
        return ApiResponse.<List<DishIngredientResponse>>builder()
                .result(dishIngredientService.saveAllDishIngredient(requestList))
                .build();
    }

    @PostMapping()
    public ApiResponse<DishIngredientResponse> createAdminDishIngredient(@RequestBody DishIngredientRequest request) {
        return ApiResponse.<DishIngredientResponse>builder()
                .result(dishIngredientService.createDishIngredient(request))
                .build();
    }


    @PutMapping("/{id}")
    public ApiResponse<DishIngredientResponse> updateDishIngredient(@PathVariable int id, @RequestBody DishIngredientRequest request) {
        return ApiResponse.<DishIngredientResponse>builder()
                .result(dishIngredientService.updateDishIngredient(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteDishIngredient(@PathVariable int id) {
        dishIngredientService.deleteDishIngredient(id);
        return ApiResponse.builder()
                .message("Nguyên liệu món ăn đã bị xóa.")
                .build();
    }

}
