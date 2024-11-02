package com.springboot.obbm.dto.menudish.request;

import com.springboot.obbm.dto.menudish.response.DishForMenuDishResponse;
import com.springboot.obbm.dto.menudish.response.MenuForMenuDishResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuDishRequest {
    Double price;
    Integer quantity;
    Integer menuId;
    Integer disheId;
}