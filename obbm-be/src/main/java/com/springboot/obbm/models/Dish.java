package com.springboot.obbm.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = "dish")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dishId;

    @Column(name = "dish_name")
    private String name;

    @Column(name = "dish_price")
    private double price;

    @Column(name = "dish_image")
    private String image;

    @Column(name = "dish_description")
    private String description;

    @Column(name = "dish_existing")
    private String existing;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categories;

    @OneToMany(mappedBy = "dishes")
    private List<MenuDish> listMenuDish;

    @OneToMany(mappedBy = "dishes")
    private List<DishIngredient> listDishIngredient;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
