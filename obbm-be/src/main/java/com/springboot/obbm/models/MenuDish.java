package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "menudish")
public class MenuDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menudishId;

    @Column(name = "menudish_price")
    private double price;

    @Column(name = "menudish_quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menus;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dishes;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
