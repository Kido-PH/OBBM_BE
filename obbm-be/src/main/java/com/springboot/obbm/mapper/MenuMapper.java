package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.menu.request.MenuCreateRequest;
import com.springboot.obbm.dto.menu.request.MenuUpdateRequest;
import com.springboot.obbm.dto.menu.response.MenuResponse;
import com.springboot.obbm.model.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "events", ignore = true)
    Menu toMenu(MenuCreateRequest request);

    MenuResponse toMenuResponse(Menu menu);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "events", ignore = true)
    void updateMenu(@MappingTarget Menu menu, MenuUpdateRequest request);
}

