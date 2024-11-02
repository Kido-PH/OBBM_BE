package com.springboot.obbm.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.obbm.util.StringFieldTrimmer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity(name = "menudish")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contractId")
public class MenuDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer menudishId;

    @Column(name = "menudish_price")
    Double price;

    @Column(name = "menudish_quantity")
    Integer quantity;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    Menu menus;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    Dish dishes;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void trimFields(){
        StringFieldTrimmer.trimAndNormalizeStringFields(this);
    }
}
