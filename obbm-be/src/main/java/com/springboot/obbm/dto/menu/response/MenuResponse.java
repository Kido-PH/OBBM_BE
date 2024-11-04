package com.springboot.obbm.dto.menu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class MenuResponse {
    Integer menuId;
    String name;
    Double totalcost;
    String description;
    UserForMenuResponse users;
    EventForMenuResponse events;
    List<MenuDishForMenuResponse> listMenuDish;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}