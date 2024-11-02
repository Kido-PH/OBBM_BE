package com.springboot.obbm.dto.menudish.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuDishResponse {
    Integer menudishId;
    Double price;
    Integer quantity;
    MenuForMenuDishResponse menus;
    DishForMenuDishResponse dishes;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}