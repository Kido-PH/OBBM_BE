package com.springboot.obbm.mapper;

import com.springboot.obbm.dto.ingredient.request.IngredientRequest;
import com.springboot.obbm.dto.ingredient.response.IngredientResponse;
import com.springboot.obbm.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredient toIngredient(IngredientRequest request);
    IngredientResponse toIngredientResponse(Ingredient Ingredient);
    void updateIngredient(@MappingTarget Ingredient Ingredient, IngredientRequest  request);
}
