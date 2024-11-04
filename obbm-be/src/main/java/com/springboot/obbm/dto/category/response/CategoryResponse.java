package com.springboot.obbm.dto.category.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.dish.response.DishResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    int categoryId;
    String name;
    String description;
    List<DishResponse> listDish;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}