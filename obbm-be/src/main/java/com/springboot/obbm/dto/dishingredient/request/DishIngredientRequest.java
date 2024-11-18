package com.springboot.obbm.dto.dishingredient.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishIngredientRequest {
    String quantity;
    String desc;
    Integer dishId;
    Integer ingredientId;
}