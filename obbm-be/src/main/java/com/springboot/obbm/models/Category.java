package com.springboot.obbm.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_description")
    private String description;

    @OneToMany(mappedBy = "categories")
    private List<Dish> listDish;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
