package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.menudish.request.MenuDishRequest;
import com.springboot.obbm.dto.menudish.response.MenuDishResponse;
import com.springboot.obbm.model.MenuDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuDishMapper {
    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    MenuDish toMenuDish(MenuDishRequest request);


    MenuDishResponse toMenuDishResponse(MenuDish menuDish);

    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    void updateMenuDish(@MappingTarget MenuDish menuDish, MenuDishRequest request);

    List<MenuDish> toMenuDishList(List<MenuDishRequest> requests);
    List<MenuDishResponse> toMenuDishResponseList(List<MenuDish> dishes);

}
