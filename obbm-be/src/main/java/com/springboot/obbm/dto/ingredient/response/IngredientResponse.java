package com.springboot.obbm.dto.ingredient.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientResponse {
    Integer ingredientId;
    String name;
    String unit;
    String desc;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
