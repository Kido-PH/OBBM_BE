package com.springboot.obbm.dto.dish.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryForDishResponse {
    int categoryId;
    String name;
    String description;
}