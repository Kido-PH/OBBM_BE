package com.springboot.obbm.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.obbm.util.StringFieldTrimmer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "dish")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contractId")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer dishId;

    @Column(name = "dish_name")
    String name;

    @Column(name = "dish_price")
    Double price;

    @Column(name = "dish_image")
    String image;

    @Column(name = "dish_description")
    String description;

    @Column(name = "dish_existing")
    String existing;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category categories;

    @OneToMany(mappedBy = "dishes")
    List<MenuDish> listMenuDish;

    @OneToMany(mappedBy = "dishes")
    List<DishIngredient> listDishIngredient;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void trimFields(){
        StringFieldTrimmer.trimAndNormalizeStringFields(this);
    }
}