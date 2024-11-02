package com.springboot.obbm.controller;

import com.springboot.obbm.dto.menudish.response.MenuDishResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.MenuDishService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menudish")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuDishController {
    private MenuDishService menuDishService;

    @GetMapping
    ApiResponse<PageImpl<MenuDishResponse>> getAllDishes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<MenuDishResponse>>builder()
                    .result(menuDishService.getAllMenuDishs(adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<MenuDishResponse> getDish(@PathVariable int id) {
        return ApiResponse.<MenuDishResponse>builder()
                .result(menuDishService.getMenuDishById(id))
                .build();
    }

//    @PostMapping
//    ApiResponse<MenuMenuDishResponse> createDish(@RequestBody @Valid DishRequest request) {
//        return ApiResponse.<MenuMenuDishResponse>builder()
//                .result(menuDishService.createDish(request))
//                .build();
//    }
//
//    @PutMapping("/{id}")
//    ApiResponse<MenuMenuDishResponse> updateDish(@PathVariable int id, @RequestBody @Valid DishRequest request) {
//        return ApiResponse.<MenuMenuDishResponse>builder()
//                .result(menuDishService.updateDish(id, request))
//                .build();
//    }
//
//    @DeleteMapping("/{id}")
//    ApiResponse<?> deleteDish(@PathVariable int id) {
//        menuDishService.deleteDish(id);
//        return ApiResponse.builder()
//                .message("Món ăn đã bị xóa.")
//                .build();
//    }

}
