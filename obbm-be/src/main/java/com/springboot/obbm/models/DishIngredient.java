package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "dishingredient")
public class DishIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dishingredientId;

    @Column(name = "dishingredient_quantity")
    private String quantity;

    @Column(name = "dishingredient_desc")
    private String desc;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dishes;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredients;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
