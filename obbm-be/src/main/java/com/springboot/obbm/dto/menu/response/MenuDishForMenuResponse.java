package com.springboot.obbm.dto.menu.response;

import com.springboot.obbm.dto.menudish.response.DishForMenuDishResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuDishForMenuResponse {
    Integer menudishId;
    Double price;
    Integer quantity;
    DishForMenuDishResponse dishes;
}