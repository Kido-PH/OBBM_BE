package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "dishingredient")
public class DishIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dishIngredientId;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dishes;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredients;

    @Column(name = "dishingredient_quantity")
    private int dishIngredientQuantity;
}
