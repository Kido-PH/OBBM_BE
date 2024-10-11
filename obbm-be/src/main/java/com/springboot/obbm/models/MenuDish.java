package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "menudish")
public class MenuDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuDishId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menus;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dishes;

    @Column(name = "menudish_price")
    private double menuDishPrice;

    @Column(name = "menudish_quantity")
    private int menuDishQuantity;
}
