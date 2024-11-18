package com.springboot.obbm.dto.ingredient.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientRequest {
    String name;
    String unit;
    String desc;
}
