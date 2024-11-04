package com.springboot.obbm.dto.menudish.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDishResponse {
    Integer menudishId;
    Double price;
    Integer quantity;
    MenuForMenuDishResponse menus;
    DishForMenuDishResponse dishes;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}