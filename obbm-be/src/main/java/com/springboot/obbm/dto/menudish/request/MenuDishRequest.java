package com.springboot.obbm.dto.menudish.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuDishRequest {
    Double price;
    Integer quantity;
    Integer menuId;
    Integer dishesId;
}