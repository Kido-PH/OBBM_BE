package com.springboot.obbm.controller;

import com.springboot.obbm.dto.menudish.request.MenuDishRequest;
import com.springboot.obbm.dto.menudish.response.MenuDishResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.MenuDishService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menudish")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuDishController {
    private MenuDishService menuDishService;

    @GetMapping
    ApiResponse<PageImpl<MenuDishResponse>> getAllMenuDishes(
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

    @GetMapping("/byMenu")
    public ApiResponse<PageImpl<MenuDishResponse>> getMenuDishByMenuId(
            @RequestParam int menuId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<MenuDishResponse>>builder()
                    .result(menuDishService.getMenuDishByMenuId(menuId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/byDish")
    public ApiResponse<PageImpl<MenuDishResponse>> getMenuDishByDishId(
            @RequestParam int dishId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<MenuDishResponse>>builder()
                    .result(menuDishService.getMenuDishByDishId(dishId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    ApiResponse<MenuDishResponse> getMenuDish(@PathVariable int id) {
        return ApiResponse.<MenuDishResponse>builder()
                .result(menuDishService.getMenuDishById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<MenuDishResponse> createMenuDish(@RequestBody MenuDishRequest request) {
        return ApiResponse.<MenuDishResponse>builder()
                .result(menuDishService.createMenuDish(request))
                .build();
    }

    @PostMapping("/saveAllMenuDish")
    public ApiResponse<List<MenuDishResponse>> saveAllMenuDish(@RequestBody List<MenuDishRequest> requestList) {
        return ApiResponse.<List<MenuDishResponse>>builder()
                .result(menuDishService.saveAllMenuDish(requestList))
                .build();
    }


    @PutMapping("/{id}")
    public ApiResponse<MenuDishResponse> updateMenuDish(@PathVariable int id, @RequestBody MenuDishRequest request) {
        return ApiResponse.<MenuDishResponse>builder()
                .result(menuDishService.updateMenuDish(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteMenuDish(@PathVariable int id) {
        menuDishService.deleteMenuDish(id);
        return ApiResponse.builder()
                .message("Thực đơn món ăn đã bị xóa.")
                .build();
    }

}
