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
public class IngredientForEventServicesResponse {
    Integer ingredientId;
    String name;
    String unit;
    String desc;
}