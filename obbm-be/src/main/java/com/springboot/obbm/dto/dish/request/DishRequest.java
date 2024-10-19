package com.springboot.obbm.dto.dish.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishRequest {
    private String name;
    private double price;
    private String image;
    private String description;
    private String existing;
    private int categoryId;
}
