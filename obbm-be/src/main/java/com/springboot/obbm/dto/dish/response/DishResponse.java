package com.springboot.obbm.dto.dish.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishResponse {
    private int dishId;
    private String name;
    private double price;
    private String image;
    private String description;
    private String existing;
}
