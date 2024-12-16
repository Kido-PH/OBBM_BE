package com.springboot.obbm.dto.menu.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuResponse {
    Integer menuId;
    String name;
    Double totalcost;
    String description;
    Boolean ismanaged;
    UserForMenuResponse users;
    EventForMenuResponse events;
    List<MenuDishForMenuResponse> listMenuDish;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

}