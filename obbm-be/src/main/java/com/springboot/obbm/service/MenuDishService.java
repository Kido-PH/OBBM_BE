package com.springboot.obbm.service;

import com.springboot.obbm.dto.menudish.response.MenuDishResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.MenuDishMapper;
import com.springboot.obbm.models.*;
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

//    public MenuDishResponse getMenuDishByMenuId(int id) {
//        return menuDishMapper.toMenuDishResponse(menuDishRespository.findByMenuDishIdAndDeletedAtIsNull(id)
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn món ăn")));
//    }

//    public MenuResponse createMenu(MenuCreateRequest request) {
//        User user = menuDishRespository.findById(request.getUserId())
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
//        Event event = dishRespository.findById(request.getEventId())
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
//        Menu menu = menuMapper.toMenu(request);
//        menu.setCreatedAt(LocalDateTime.now());
//        menu.setUsers(user);
//        menu.setEvents(event);
//        return menuMapper.toMenuResponse(menuRespository.save(menu));
//    }
//
//    public MenuResponse updateMenu(int id, MenuUpdateRequest request) {
//        Menu menu = menuRespository.findByMenuIdAndDeletedAtIsNull(id).orElseThrow(
//                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
//        Event event = dishRespository.findById(request.getEventId())
//                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
//        menu.setCreatedAt(LocalDateTime.now());
//        menu.setEvents(event);
//        menuMapper.updateMenu(menu, request);
//        return menuMapper.toMenuResponse(menuRespository.save(menu));
//    }
//
//    public void deleteMenu(int id) {
//        Menu menu = menuRespository.findByMenuIdAndDeletedAtIsNull(id).orElseThrow(
//                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
//
//        menu.setDeletedAt(LocalDateTime.now());
//        menuRespository.save(menu);
//    }
}
