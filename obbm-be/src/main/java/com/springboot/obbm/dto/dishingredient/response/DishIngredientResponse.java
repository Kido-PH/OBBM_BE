package com.springboot.obbm.dto.dishingredient.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.dish.response.DishResponse;
import com.springboot.obbm.dto.ingredient.response.IngredientResponse;
import com.springboot.obbm.model.Dish;
import com.springboot.obbm.model.Ingredient;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishIngredientResponse {
    Integer dishingredientId;
    String quantity;
    String desc;
    DishResponse dishes;
    IngredientResponse ingredients;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}