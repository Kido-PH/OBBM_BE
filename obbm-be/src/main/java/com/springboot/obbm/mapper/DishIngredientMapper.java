package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.dishingredient.request.DishIngredientRequest;
import com.springboot.obbm.dto.dishingredient.response.DishIngredientResponse;
import com.springboot.obbm.model.DishIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishIngredientMapper {
    @Mapping(target = "dishes", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    DishIngredient toDishIngredient(DishIngredientRequest request);

    DishIngredientResponse toDishIngredientResponse(DishIngredient DishIngredient);

    @Mapping(target = "dishes", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    void updateDishIngredient(@MappingTarget DishIngredient DishIngredient, DishIngredientRequest request);

    List<DishIngredientResponse> toDishIngredientResponseList(List<DishIngredient> DishIngredient);
}
