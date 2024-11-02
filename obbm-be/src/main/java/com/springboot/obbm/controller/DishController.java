package com.springboot.obbm.controller;

import com.springboot.obbm.dto.dish.request.DishRequest;
import com.springboot.obbm.dto.dish.response.DishResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.DishService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DishController {
    private DishService dishService;

    @GetMapping
    ApiResponse<PageImpl<DishResponse>> getAllDishes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<DishResponse>>builder()
                    .result(dishService.getAllDishes(adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<DishResponse> getDish(@PathVariable int id) {
        return ApiResponse.<DishResponse>builder()
                .result(dishService.getDishById(id))
                .build();
    }

    @PostMapping
    ApiResponse<DishResponse> createDish(@RequestBody @Valid DishRequest request) {
        return ApiResponse.<DishResponse>builder()
                .result(dishService.createDish(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<DishResponse> updateDish(@PathVariable int id, @RequestBody @Valid DishRequest request) {
        return ApiResponse.<DishResponse>builder()
                .result(dishService.updateDish(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteDish(@PathVariable int id) {
        dishService.deleteDish(id);
        return ApiResponse.builder()
                .message("Món ăn đã bị xóa.")
                .build();
    }

}
