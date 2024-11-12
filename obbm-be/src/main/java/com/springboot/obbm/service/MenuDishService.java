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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MenuDishService {
    MenuDishRepository menuDishRepository;
    MenuRepository menuRepository;
    DishRepository dishRepository;
    MenuDishMapper menuDishMapper;

    public List<MenuDishResponse> saveAllMenuDish(List<MenuDishRequest> requestList) {
        List<MenuDish> menuDishList = new ArrayList<>();
        for (MenuDishRequest request : requestList) {
            Menu menu = menuRepository.findById(request.getMenuId())
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));

            Dish dish = dishRepository.findByDishIdAndDeletedAtIsNull(request.getDishesId())
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));

            MenuDish menuDish = menuDishMapper.toMenuDish(request);
            menuDish.setCreatedAt(LocalDateTime.now());
            menuDish.setMenus(menu);
            menuDish.setDishes(dish);

            menuDishList.add(menuDish);
        }


        return menuDishMapper.toMenuDishResponseList(menuDishRepository.saveAll(menuDishList));
    }

    public PageImpl<MenuDishResponse> getAllMenuDishs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuDish> menuDishPage = menuDishRepository.findAllByDeletedAtIsNull(pageable);

        var responseList = menuDishPage.getContent().stream()
                .distinct().map(menuDishMapper::toMenuDishResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, menuDishPage.getTotalElements());
    }

    public MenuDishResponse getMenuDishById(int id) {
        return menuDishMapper.toMenuDishResponse(menuDishRepository.findByMenudishIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn")));
    }

    public PageImpl<MenuDishResponse> getMenuDishByMenuId(int menuId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuDish> menuDishPage = menuDishRepository.findAllByMenus_MenuIdAndDeletedAtIsNull(menuId, pageable);

        var responseList = menuDishPage.getContent().stream()
                .map(menuDishMapper::toMenuDishResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, menuDishPage.getTotalElements());
    }

    public PageImpl<MenuDishResponse> getMenuDishByDishId(int dishId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuDish> menuDishPage = menuDishRepository.findAllByDishes_DishIdAndDeletedAtIsNull(dishId, pageable);

        var responseList = menuDishPage.getContent().stream()
                .map(menuDishMapper::toMenuDishResponse)
                .toList();
        return new PageImpl<>(responseList, pageable, menuDishPage.getTotalElements());
    }

    public MenuDishResponse createMenuDish(MenuDishRequest request) {
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
        Dish dish = dishRepository.findById(request.getDishesId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));
        MenuDish menuDish = menuDishMapper.toMenuDish(request);
        menuDish.setCreatedAt(LocalDateTime.now());
        menuDish.setMenus(menu);
        menuDish.setDishes(dish);
        return menuDishMapper.toMenuDishResponse(menuDishRepository.save(menuDish));
    }

    public MenuDishResponse updateMenuDish(int id, MenuDishRequest request) {
        MenuDish menuDish = menuDishRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn"));
        Dish dish = dishRepository.findById(request.getDishesId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Món ăn"));
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
        menuDish.setUpdatedAt(LocalDateTime.now());
        menuDish.setMenus(menu);
        menuDish.setDishes(dish);
        menuDishMapper.updateMenuDish(menuDish, request);
        return menuDishMapper.toMenuDishResponse(menuDishRepository.save(menuDish));
    }

    public void deleteMenuDish(int id) {
        MenuDish menuDish = menuDishRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn"));

        menuDish.setDeletedAt(LocalDateTime.now());
        menuDishRepository.save(menuDish);
    }
}
