package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ingredientId;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "ingredient_unit")
    private String ingredientUnit;

    @Column(name = "ingredient_transdate")
    private Date ingredientTransDate;

    @Column(name = "ingredient_desc")
    private String ingredientDesc;

    @Column(name = "isdeleted")
    private boolean isDeleted;
}
