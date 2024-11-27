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
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<MenuResponse>>builder()
                    .result(menuService.getAllMenus(adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllMenuUser")
    ApiResponse<PageImpl<MenuResponse>> getAllUserMenus(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<MenuResponse>>builder()
                    .result(menuService.getAllUserOrAdminMenus(adjustedPage, size, false))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllMenuAdmin")
    ApiResponse<PageImpl<MenuResponse>> getAllAdminOrUserMenus(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<MenuResponse>>builder()
                    .result(menuService.getAllUserOrAdminMenus(adjustedPage, size, true))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAllMenu/{userId}")
    ApiResponse<PageImpl<MenuResponse>> getAllMenusByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            int adjustedPage = (page > 0) ? page - 1 : 0;
            return ApiResponse.<PageImpl<MenuResponse>>builder()
                    .result(menuService.getAllMenuByUserId(userId, adjustedPage, size))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/latestMenu/{userId}")
    ApiResponse<MenuResponse> getLatestMenuByUserId(@PathVariable String userId) {
        return ApiResponse.<MenuResponse>builder()
                .result(menuService.getLatestMenuByUserId(userId))
                .build();
    }

    @GetMapping("/{menuId}")
    ApiResponse<MenuResponse> getMenu(@PathVariable int menuId) {
        return ApiResponse.<MenuResponse>builder()
                .result(menuService.getMenuById(menuId))
                .build();
    }

    @PostMapping("/admin")
    ApiResponse<MenuResponse> createAdminMenu(@RequestBody MenuCreateRequest request) {
        return ApiResponse.<MenuResponse>builder()
                .result(menuService.createAdminMenu(request))
                .build();
    }

    @PostMapping("/user")
    ApiResponse<MenuResponse> createUserMenu(@RequestBody MenuCreateRequest request) {
        return ApiResponse.<MenuResponse>builder()
                .result(menuService.createUserMenu(request))
                .build();
    }

    @PutMapping("/{menuId}")
    ApiResponse<MenuResponse> updateMenu(@PathVariable int menuId, @RequestBody MenuUpdateRequest request) {
        return ApiResponse.<MenuResponse>builder()
                .result(menuService.updateMenu(menuId, request))
                .build();
    }

    @DeleteMapping("/{menuId}")
    ApiResponse<?> deleteMenu(@PathVariable int menuId) {
        menuService.deleteMenu(menuId);
        return ApiResponse.builder()
                .message("Thực đơn đã bị xóa.")
                .build();
    }
}
