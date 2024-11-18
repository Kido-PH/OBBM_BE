package com.springboot.obbm.dto.dishingredient.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishIngredientResponse {
    Integer dishingredientId;
    String quantity;
    String desc;
    Dish dishes;
    Ingredient ingredients;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}