package com.springboot.obbm.dto.dishingredient.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishForDishIngredientResponse {
    Integer dishId;
    String name;
    Double price;
    String image;
    String description;
    String existing;
}