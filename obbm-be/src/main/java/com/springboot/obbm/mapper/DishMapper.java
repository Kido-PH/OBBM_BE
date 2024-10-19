package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.dish.request.DishRequest;
import com.springboot.obbm.dto.dish.response.DishResponse;
import com.springboot.obbm.models.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DishMapper {
    @Mapping(target = "categories", ignore = true)
    Dish toDish(DishRequest request);
    DishResponse toDishResponse(Dish dish);
    @Mapping(target = "categories", ignore = true)
    void upadteDish(@MappingTarget Dish dish, DishRequest request);
}
