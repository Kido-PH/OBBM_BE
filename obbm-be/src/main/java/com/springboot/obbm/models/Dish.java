package com.springboot.obbm.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity(name = "dish")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dishId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category cates;

    @Column(name = "dish_name")
    private String dishName;

    @Column(name = "dish_image")
    private String dishImage;

    @Column(name = "dish_price")
    private double dishPrice;

    @Column(name = "dish_existing")
    private String dishExisting;

    @Column(name = "dish_description")
    private String dishDescription;

    @Column(name = "isdeleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "dishes")
    private Set<MenuDish> listMenuDish;

    @OneToMany(mappedBy = "dishes")
    private Set<DishIngredient> listDishIngredient;
}
