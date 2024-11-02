package com.springboot.obbm.dto.menudish.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuForMenuDishResponse {
    Integer menuId;
    String name;
}