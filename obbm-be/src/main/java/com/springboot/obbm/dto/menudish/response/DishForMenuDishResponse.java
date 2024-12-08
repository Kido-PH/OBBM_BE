package com.springboot.obbm.dto.menudish.response;

import com.springboot.obbm.dto.dish.response.CategoryForDishResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishForMenuDishResponse {
    Integer dishId;
    String name;
    Double price;
    String image;
    String description;
    String existing;
    CategoryForDishResponse categories;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}