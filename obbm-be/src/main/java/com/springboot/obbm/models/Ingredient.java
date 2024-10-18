package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ingredientId;

    @Column(name = "ingredient_name")
    private String name;

    @Column(name = "ingredient_unit")
    private String unit;

    @Column(name = "ingredient_transdate")
    private Date transdate;

    @Column(name = "ingredient_desc")
    private String desc;

    @OneToMany(mappedBy = "ingredients")
    private List<StockRequest> listStockrequest;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
