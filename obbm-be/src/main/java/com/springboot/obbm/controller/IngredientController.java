package com.springboot.obbm.controller;

import com.springboot.obbm.dto.ingredient.request.IngredientRequest;
import com.springboot.obbm.dto.ingredient.response.IngredientResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.IngredientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredient")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IngredientController {
    private IngredientService ingredientService;

    @GetMapping
    ApiResponse<PageImpl<IngredientResponse>> getAllIngredient(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<IngredientResponse>>builder()
                    .result(ingredientService.getAllIngredients(adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<IngredientResponse> getDish(@PathVariable int id) {
        return ApiResponse.<IngredientResponse>builder()
                .result(ingredientService.getIngredientById(id))
                .build();
    }

    @PostMapping
    ApiResponse<IngredientResponse> createDish(@RequestBody IngredientRequest request) {
        return ApiResponse.<IngredientResponse>builder()
                .result(ingredientService.createIngredient(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<IngredientResponse> updateDish(@PathVariable int id, @RequestBody IngredientRequest request) {
        return ApiResponse.<IngredientResponse>builder()
                .result(ingredientService.updateIngredient(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteDish(@PathVariable int id) {
        ingredientService.deleteIngredient(id);
        return ApiResponse.builder()
                .message("Nguyên liệu đã bị xóa.")
                .build();
    }

}
