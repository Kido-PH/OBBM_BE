package com.springboot.obbm.dto.dish.response;

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
public class DishResponse {
    private int dishId;
    private String name;
    private double price;
    private String image;
    private String description;
    private String existing;
    CategoryForDishResponse categories;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
