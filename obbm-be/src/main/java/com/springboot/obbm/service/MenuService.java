package com.springboot.obbm.service;

import com.springboot.obbm.dto.menu.request.MenuCreateRequest;
import com.springboot.obbm.dto.menu.request.MenuUpdateRequest;
import com.springboot.obbm.dto.menu.response.MenuResponse;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.mapper.MenuMapper;
import com.springboot.obbm.model.Event;
import com.springboot.obbm.model.Menu;
import com.springboot.obbm.model.User;
import com.springboot.obbm.respository.EventRespository;
import com.springboot.obbm.respository.MenuRespository;
import com.springboot.obbm.respository.UserRespository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MenuService {
    MenuRespository menuRespository;
    UserRespository userRespository;
    EventRespository eventRespository;
    MenuMapper menuMapper;

    public PageImpl<MenuResponse> getAllMenus(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> menuPage = menuRespository.findAllByDeletedAtIsNull(pageable);

        var responseList = menuPage.getContent().stream()
                .map(menu -> {
                    menu.setListMenuDish(
                            menu.getListMenuDish().stream()
                                    .filter(menuDish -> menuDish.getDeletedAt() == null)
                                    .collect(Collectors.toList())
                    );
                    return menuMapper.toMenuResponse(menu);
                })
                .distinct()
                .toList();

        return new PageImpl<>(responseList, pageable, menuPage.getTotalElements());
    }

    public MenuResponse getMenuById(int id) {
        Menu menu = menuRespository.findByMenuIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));

        menu.setListMenuDish(
                menu.getListMenuDish().stream()
                        .filter(menuDish -> menuDish.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );

        return menuMapper.toMenuResponse(menu);
    }

    public MenuResponse createMenu(MenuCreateRequest request) {
        User user = userRespository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        Event event = eventRespository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        Menu menu = menuMapper.toMenu(request);
        menu.setCreatedAt(LocalDateTime.now());
        menu.setUsers(user);
        menu.setEvents(event);
        return menuMapper.toMenuResponse(menuRespository.save(menu));
    }

    public MenuResponse updateMenu(int id, MenuUpdateRequest request) {
        Menu menu = menuRespository.findByMenuIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
        Event event = eventRespository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        menu.setCreatedAt(LocalDateTime.now());
        menu.setEvents(event);
        menuMapper.updateMenu(menu, request);
        return menuMapper.toMenuResponse(menuRespository.save(menu));
    }

    public void deleteMenu(int id) {
        Menu menu = menuRespository.findByMenuIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));

        menu.setDeletedAt(LocalDateTime.now());
        menuRespository.save(menu);
    }
}
