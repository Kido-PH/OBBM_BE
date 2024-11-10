package com.springboot.obbm.controller;

import com.springboot.obbm.dto.menu.request.MenuCreateRequest;
import com.springboot.obbm.dto.menu.request.MenuUpdateRequest;
import com.springboot.obbm.dto.menu.response.MenuResponse;
import com.springboot.obbm.dto.response.ApiResponse;
import com.springboot.obbm.service.MenuService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuController {
    private MenuService menuService;

    @GetMapping
    ApiResponse<PageImpl<MenuResponse>> getAllMenus(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try{
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<MenuResponse>>builder()
                    .result(menuService.getAllMenus(adjustedPage,size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/latestMenu")
    ApiResponse<MenuResponse> getLatestMenu() {
        return ApiResponse.<MenuResponse>builder()
                .result(menuService.getLatestMenuByUserId())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<MenuResponse> getCategory(@PathVariable int id) {
        return ApiResponse.<MenuResponse>builder()
                .result(menuService.getMenuById(id))
                .build();
    }

    @PostMapping
    ApiResponse<MenuResponse> createCategory(@RequestBody MenuCreateRequest request){
        return ApiResponse.<MenuResponse>builder()
                .result(menuService.createMenu(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<MenuResponse> updateCategory(@PathVariable int id, @RequestBody MenuUpdateRequest request){
        return ApiResponse.<MenuResponse>builder()
                .result(menuService.updateMenu(id,request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteCategory(@PathVariable int id) {
        menuService.deleteMenu(id);
        return ApiResponse.builder()
                .message("Thực đơn đã bị xóa.")
                .build();
    }

}
