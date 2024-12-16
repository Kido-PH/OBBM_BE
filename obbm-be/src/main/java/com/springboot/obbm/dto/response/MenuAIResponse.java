package com.springboot.obbm.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuAIResponse {
    private List<DishAIResponse> listDish;
    @Data
    @AllArgsConstructor
    public static class DishAIResponse {
        private int dishId;
        private String name;
        private double price;
    }
    private double totalCost;
}
