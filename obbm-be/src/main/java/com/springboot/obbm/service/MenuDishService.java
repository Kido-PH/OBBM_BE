package com.springboot.obbm.service;

import com.springboot.obbm.dto.menudish.request.MenuDishRequest;
import com.springboot.obbm.dto.menudish.response.MenuDishResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.MenuDishMapper;
import com.springboot.obbm.model.*;
import com.springboot.obbm.respository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MenuDishService {
    MenuDishRespository menuDishRespository;
    MenuRespository menuRespository;
    DishRespository dishRespository;
    MenuDishMapper menuDishMapper;

    public PageImpl<MenuDishResponse> getAllMenuDishs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuDish> menuDishPage = menuDishRespository.findAllByDeletedAtIsNull(pageable);

        var responseList = menuDishPage.getContent().stream()
                .distinct().map(menuDishMapper::toMenuDishResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, menuDishPage.getTotalElements());
    }

    public MenuDishResponse getMenuDishById(int id) {
        return menuDishMapper.toMenuDishResponse(menuDishRespository.findByMenudishIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn")));
    }

    public PageImpl<MenuDishResponse> getMenuDishByMenuId(int menuId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuDish> menuDishPage = menuDishRespository.findAllByMenus_MenuIdAndDeletedAtIsNull(menuId, pageable);

        var responseList = menuDishPage.getContent().stream()
                .map(menuDishMapper::toMenuDishResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, menuDishPage.getTotalElements());
    }

    public PageImpl<MenuDishResponse> getMenuDishByDishId(int dishId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuDish> menuDishPage = menuDishRespository.findAllByDishes_DishIdAndDeletedAtIsNull(dishId, pageable);

        var responseList = menuDishPage.getContent().stream()
                .map(menuDishMapper::toMenuDishResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, menuDishPage.getTotalElements());
    }

    public MenuDishResponse createMenuDish(MenuDishRequest request) {
        Menu menu = menuRespository.findById(request.getMenuId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
        Dish dish = dishRespository.findById(request.getDishesId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));
        MenuDish menuDish = menuDishMapper.toMenuDish(request);
        menuDish.setCreatedAt(LocalDateTime.now());
        menuDish.setMenus(menu);
        menuDish.setDishes(dish);
        return menuDishMapper.toMenuDishResponse(menuDishRespository.save(menuDish));
    }

    public MenuDishResponse updateMenuDish(int id, MenuDishRequest request) {
        MenuDish menuDish = menuDishRespository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn"));
        Dish dish = dishRespository.findById(request.getDishesId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));
        Menu menu = menuRespository.findById(request.getMenuId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
        menuDish.setUpdatedAt(LocalDateTime.now());
        menuDish.setMenus(menu);
        menuDish.setDishes(dish);
        menuDishMapper.updateMenuDish(menuDish, request);
        return menuDishMapper.toMenuDishResponse(menuDishRespository.save(menuDish));
    }

    public void deleteMenuDish(int id) {
        MenuDish menuDish = menuDishRespository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn"));

        menuDish.setDeletedAt(LocalDateTime.now());
        menuDishRespository.save(menuDish);
    }
}
