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
import com.springboot.obbm.respository.EventRepository;
import com.springboot.obbm.respository.MenuRepository;
import com.springboot.obbm.respository.UserRepository;
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
    MenuRepository menuRepository;
    UserRepository userRepository;
    EventRepository eventRepository;
    MenuMapper menuMapper;

    public PageImpl<MenuResponse> getAllMenus(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> menuPage = menuRepository.findAllByDeletedAtIsNullOrderByMenuIdDesc(pageable);

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

    public PageImpl<MenuResponse> getAllUserOrAdminMenus(int page, int size, boolean isAdmin) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> menuPage = menuRepository.findAllByIsmanagedAndDeletedAtIsNull(isAdmin, pageable);

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

    public PageImpl<MenuResponse> getAllMenuByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> menuPage = menuRepository.findAllByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId, pageable);

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

    public MenuResponse getLatestMenuByUserId(String userId) {
        Menu menu = menuRepository.findTopByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));

        menu.setListMenuDish(
                menu.getListMenuDish().stream()
                        .filter(menuDish -> menuDish.getDeletedAt() == null)
                        .collect(Collectors.toList())
        );

        return menuMapper.toMenuResponse(menu);
    }

    public MenuResponse getMenuById(int id) {
        Menu menu = menuRepository.findByMenuIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));

        menu.setListMenuDish(
                menu.getListMenuDish().stream()
                        .filter(menuDish -> menuDish.getDeletedAt() == null)
                        .collect(Collectors.toList())

        );

        return menuMapper.toMenuResponse(menu);
    }

    public MenuResponse createAdminMenu(MenuCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        Menu menu = menuMapper.toMenu(request);
        menu.setIsmanaged(true);
        menu.setCreatedAt(LocalDateTime.now());
        menu.setUsers(user);
        menu.setEvents(event);
        return menuMapper.toMenuResponse(menuRepository.save(menu));
    }

    public MenuResponse createUserMenu(MenuCreateRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
        Menu menu = menuMapper.toMenu(request);
        menu.setCreatedAt(LocalDateTime.now());
        menu.setIsmanaged(false);
        menu.setUsers(user);
        menu.setEvents(event);
        return menuMapper.toMenuResponse(menuRepository.save(menu));
    }

    public MenuResponse updateMenu(int id, MenuUpdateRequest request) {
        Menu menu = menuRepository.findByMenuIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Sự kiện"));
        menu.setCreatedAt(LocalDateTime.now());
        menu.setEvents(event);
        menuMapper.updateMenu(menu, request);
        return menuMapper.toMenuResponse(menuRepository.save(menu));
    }

    public void deleteMenu(int id) {
        Menu menu = menuRepository.findByMenuIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Thực đơn"));

        menu.setDeletedAt(LocalDateTime.now());
        menuRepository.save(menu);
    }
}
